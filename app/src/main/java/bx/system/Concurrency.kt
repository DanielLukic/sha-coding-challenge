package bx.system

import android.os.Handler
import android.os.Looper
import bx.logging.Log
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicReference

class Concurrency(
    private val handler: Handler = Handler(Looper.getMainLooper())
) {
    private fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

    fun verifyMainThread() {
        if (!isMainThread()) Log.error { "not on main thread: ${Thread.currentThread().name}" }
    }

    fun ensureMainThread() {
        if (!isMainThread()) Log.error(WrongThread())
        if (!isMainThread()) throw WrongThread()
    }

    fun ensureWorkerThread() {
        if (isMainThread()) Log.error(WrongThread())
        if (isMainThread()) throw WrongThread()
    }

    fun <T> syncOnMainThread(block: () -> T): T = if (isMainThread()) {
        block()
    } else {
        val value = AtomicReference<T>()
        val lock = Semaphore(0)
        var error: Throwable? = null
        handler.post {
            try {
                value.set(block())
            } catch (it: Throwable) {
                Log.error { it }
                error = it
            }
            lock.release()
        }
        lock.acquire()
        error?.let { throw it } ?: value.get()
    }

    class WrongThread : RuntimeException()
}
