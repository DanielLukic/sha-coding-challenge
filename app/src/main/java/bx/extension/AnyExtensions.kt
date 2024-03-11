@file:Suppress("NOTHING_TO_INLINE")

package bx.extension

import android.os.Build.MODEL
import bx.logging.Log
import java.io.IOException

inline fun ioError(it: String): Nothing = throw IOException(it)

fun nop() {}

fun <R> runSafely(block: () -> R): R? = try {
    block()
}
catch (it: Throwable) {
    Log.error(it)
    null
}

val isRunningInEmulator
    get() = runCatching {
        MODEL.startsWith("Android SDK") || MODEL.startsWith("sdk_")
    }.getOrDefault(false)

fun <L> Collection<L>.safelyForEach(block: L.() -> Unit) = forEach {
    runCatching { block(it) }.onFailure { Log.error(it, "listener failed - ignored") }
}
