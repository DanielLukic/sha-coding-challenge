package bx.util

import bx.logging.Log
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@Suppress("unused")
class RetryWithBackoff(
    private val logTag: String,
    private val maxRetries: Int,
    private val delayMultiplier: Long,
    private val scheduler: Scheduler,
) {
    var retryIf: (Throwable) -> Boolean = { false }

    companion object {
        fun <T : Any> Observable<T>.retryWithBackoff(
            tag: String,
            maxRetries: Int = 2,
            delayMultiplier: Long = 1000,
            scheduler: Scheduler = Schedulers.io(),
            retryIf: (Throwable) -> Boolean = { true }
        ): Observable<T> = retryWhen(
            RetryWithBackoff(tag, maxRetries, delayMultiplier, scheduler).apply { this.retryIf = retryIf }.createObservable()
        )

        fun <T : Any> Single<T>.retryWithBackoff(
            tag: String,
            scheduler: Scheduler,
            retryIf: (Throwable) -> Boolean = { true }
        ): Single<T> = retryWhen(
            RetryWithBackoff(tag, 2, 1000, scheduler).apply { this.retryIf = retryIf }.createFlow()
        )

        fun Completable.retryWithBackoff(
            tag: String,
            scheduler: Scheduler,
            retryIf: (Throwable) -> Boolean = { true }
        ): Completable = retryWhen(
            RetryWithBackoff(tag, 2, 1000, scheduler).apply { this.retryIf = retryIf }.createFlow()
        )
    }

    fun createObservable() = { errors: Observable<Throwable> ->
        val count = AtomicInteger(0)
        errors
            .flatMap {
                if (mayRetry(it, count)) Observable.just(it) else Observable.error(it)
            }
            .flatMap { error ->
                val it = count.get()
                val delay = delayMultiplier * it
                Log.verbose { "$logTag: retry count: $it - delay: $delay - failed due to $error" }

                // NOTE: Without observeOn, this will switch the entire subscription to computation and in turn
                // breaks reading data or whatever the caller expects as scheduler. Therefore, explicitly requiring the
                // scheduler.
                Observable.timer(delay, TimeUnit.MILLISECONDS, scheduler)
            }
    }

    fun createFlow() = { errors: Flowable<Throwable> ->
        val count = AtomicInteger(0)
        errors
            .flatMap {
                if (mayRetry(it, count)) Flowable.just(it) else Flowable.error(it)
            }
            .flatMap { error ->
                val it = count.get()
                val delay = delayMultiplier * it
                Log.verbose { "$logTag: retry count: $it - delay: $delay - failed due to $error" }

                // NOTE: Without observeOn, this will switch the entire subscription to computation and in turn
                // breaks reading data or whatever the caller expects as scheduler. Therefore, explicitly requiring the
                // scheduler.
                Flowable.timer(delay, TimeUnit.MILLISECONDS, scheduler)
            }
    }

    private fun mayRetry(it: Throwable, count: AtomicInteger) = when {
        retryIf(it) -> count.getAndIncrement() < maxRetries
        else        -> false
    }
}
