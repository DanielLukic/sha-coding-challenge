@file:Suppress("FunctionName", "unused", "EXPERIMENTAL_API_USAGE")

package bx.util

import bx.logging.Log
import bx.system.Cx
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.*
import java.util.concurrent.Executors.newSingleThreadScheduledExecutor
import java.util.concurrent.ThreadFactory
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.time.Duration

class NamedThreadFactory(private val name: String) : ThreadFactory {
    override fun newThread(it: Runnable): Thread = Thread(it, name)
}

fun LoggedExceptions() = CoroutineExceptionHandler { context, it ->
    Log.error(it, "ignored in $context (${context[CoroutineName]}: $it")
}

fun SafeMainScope(name: String) = MainScope() + CoroutineName(name) + LoggedExceptions()
fun SafeSingleScope(name: String) = CoroutineScope(
    SupervisorJob() + CoroutineName(name) + LoggedExceptions() + SingleContext(name)
)

fun SafeViewModelScope(name: String, immediate: Boolean = true) = MainScope().run {
    if (immediate) plus(Cx.MainImmediate) else this
} + CoroutineName(name) + LoggedExceptions()

fun SingleThread(name: String) = newSingleThreadScheduledExecutor(NamedThreadFactory(name)).asCoroutineDispatcher()
fun SingleContext(name: String) = SupervisorJob() + SingleThread(name) + CoroutineName(name) + LoggedExceptions()
fun SuperIoContext(name: String) = SupervisorJob() + Cx.IO + CoroutineName(name) + LoggedExceptions()
fun SuperIoScope(name: String) = CoroutineScope(SuperIoContext(name))

fun <T> CxResult<T>.logIfFailure(): CxResult<T> = apply { failureOrNull()?.let(Log::error) }

fun <T> T.asSuccess(): CxResult<T> = CxResult.success(this)

/** See [tryCatching]. */
sealed class CxResult<R> {
    data class Success<R>(val value: R) : CxResult<R>()
    data class Failure<R>(val throwable: Throwable) : CxResult<R>()

    companion object {
        fun <R> success(value: R): CxResult<R> = Success(value)
        fun <R> failure(throwable: Throwable): CxResult<R> = Failure(throwable)
    }

    suspend fun onSuccess(block: suspend (R) -> Unit): CxResult<R> {
        if (this is Success) block(value)
        return this
    }

    suspend fun onFailure(block: suspend (Throwable) -> Unit): CxResult<R> {
        if (this is Failure) block(throwable)
        return this
    }

    val isSuccess: Boolean by lazy { this is Success }
    val isFailure: Boolean by lazy { this is Failure }

    fun valueOrNull(): R? = if (this is Success) value else null
    fun failureOrNull(): Throwable? = if (this is Failure) throwable else null

    suspend fun andFinally(block: suspend () -> Unit): CxResult<R> = apply { block() }

    suspend fun <OUT> andThen(block: suspend (R) -> CxResult<OUT>): CxResult<OUT> = when (this) {
        is Success -> block(value)
        is Failure -> failure(throwable)
    }

    suspend fun <OUT> map(block: suspend (R) -> OUT): CxResult<OUT> = when (this) {
        is Success -> success(block(value))
        is Failure -> failure(throwable)
    }
}

/**
 * In contrast to [runCatching] the [CancellationException] is not caught in
 * this variant. This /should/ make it more straight forward to use in cx code.
 *
 * Note that [TimeoutCancellationException] is not handled here!
 *
 * Note that `null` results are a failure here.
 */
suspend fun <T, R> T.tryCatching(dispatcher: CoroutineDispatcher? = null, block: suspend T.() -> R): CxResult<R> {
    val result = runCatching {
        if (dispatcher != null)
            withContext(dispatcher) { block() }
        else
            block()
    }

    // This is the difference to runCatching: We do not intercept
    // CancellationException here. We have to intercept
    // TimeoutCancellationException however, to allow handling it.
    val failure = result.exceptionOrNull()
    if (failure is CancellationException && failure !is TimeoutCancellationException) {
        throw result.exceptionOrNull() as CancellationException
    }

    return if (result.isSuccess) {
        val value = result.getOrNull()
        if (value != null)
            CxResult.success(value)
        else
            CxResult.failure(IllegalStateException("null result"))
    }
    else {
        val throwable = result.exceptionOrNull()
        if (throwable != null)
            CxResult.failure(throwable)
        else
            CxResult.failure(IllegalStateException("null throwable"))
    }
}

tailrec suspend fun <R : Any?> retryWithTimeout(
    tries: Int,
    timeout: Long,
    attempt: Int = 0,
    block: suspend CoroutineScope.() -> R,
): R {
    val (result, failure) = try {
        withTimeout(timeout, block) to null
    }
    catch (it: TimeoutCancellationException) {
        null to it
    }
    @Suppress("UNCHECKED_CAST")
    return when {
        failure == null  -> result as R
        attempt >= tries -> throw failure
        else             -> retryWithTimeout(tries, timeout, attempt + 1, block)
    }
}

@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("Remove when done migrating to CX")
fun <R : Any?> cxIO(block: suspend CoroutineScope.() -> R): R {
    return runBlocking(Dispatchers.IO + LoggedExceptions(), block)
}

@Deprecated("Remove when done migrating to CX")
suspend fun <T> suspendDetached(onCancel: (Throwable?) -> Unit = {}, block: () -> T): T = suspendCancellableCoroutine {
    val create = thread {
        runCatching { block() }
            .onFailure { throwable -> it.cancel(throwable) }
            .onSuccess { result -> it.resume(result) }
    }
    it.invokeOnCancellation { throwable -> create.interrupt(); onCancel(throwable) }
}

fun Completable.andThenCx(block: suspend CoroutineScope.() -> Unit): Completable = andThen(
    cxCompletable { block() })

// Unlike rxCompletable, this will use the rx thread:
fun cxCompletable(block: suspend CoroutineScope.() -> Unit) = Completable.create { emitter ->
    runBlocking {
        val job = launch {
            tryCatching { block() }
                .onFailure { emitter.tryOnError(it) }
                .onSuccess { emitter.onComplete() }
        }
        emitter.setCancellable { job.cancel() }
    }
}

suspend fun autoRetry(onFailure: suspend (Throwable) -> Unit = {}, call: suspend () -> Unit) = coroutineScope {
    tryCatching { call() }.onFailure { onFailure(it); if (isActive) call() }
}

suspend fun cxSafely(lambda: suspend () -> Unit) = coroutineScope {
    tryCatching { lambda() }.onFailure { Log.verbose { "cx lambda failed - ignored: $it" } }
}

suspend fun await(what: String, duration: Duration, call: suspend () -> Unit) {
    withTimeoutOrNull(duration) {
        tryCatching { call(); Log.info(what) }.onFailure { Log.error(it, "$what failed - ignored: $it") }
    } ?: Log.error("$what timed out - ignored")
}
