@file:Suppress("unused", "EXPERIMENTAL_API_USAGE")

package bx.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import bx.logging.Log
import kotlinx.coroutines.CancellationException
import okio.ByteString

suspend fun <R> loggedHere(hint: String, block: suspend () -> R): R = try {
    block()
}
catch (it: Throwable) {
    if (it !is CancellationException) Log.verbose("$hint failed: $it")
    throw it
}

fun ByteArray.pad(length: Int, byte: Byte = 0) = ByteArray(length) { getOrNull(it) ?: byte }
fun ByteArray.dropFirstByte(): ByteArray = copyOfRange(1, size - 1)
fun ByteArray.toHexString(delimiter: String = "") = joinToString(delimiter) { "%02x".format(it) }

fun String.hexToByteArray(): ByteArray {
    return mapIndexedNotNull { index, _ ->
        if (/*index != 0 || */index + 1 >= length || index % 2 != 0) null
        else "${this[index]}${this[index + 1]}"
    }.map { it.toInt(16).toByte() }.toByteArray()
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0f
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

@Suppress("OPT_IN_USAGE")
fun ByteArray.crossSum(): Int = toUByteArray().map { it.toInt() }.reduce { acc, i -> acc + i }

fun ByteArray.toInt32(): Int {
    if (size != 4) error("unsupported size")
    val (a, b, c, d) = map { it.toUByte().toInt() }
    return (a shl 24) or (b shl 16) or (c shl 8) or d
}

fun Int.toByteArray(): ByteArray {
    val firstByte = shr(24)
    val firstByteValue = firstByte.shl(24)
    val secondByte = (this - firstByteValue).shr(16)
    val secondByteValue = secondByte.shl(16)
    val thirdByte = (this - firstByteValue - secondByteValue).shr(8)
    val thirdByteValue = thirdByte.shl(8)
    val fourthByte = this - firstByteValue - secondByteValue - thirdByteValue
    return ByteArray(4) { index ->
        when (index) {
            0    -> firstByte
            1    -> secondByte
            2    -> thirdByte
            else -> fourthByte
        }.toByte()
    }
}

fun ByteArray.chunk(packageSize: Int): List<ByteArray> = asList()
    .chunked(packageSize)
    .map { it.toByteArray() }

operator fun <T> Set<T>.get(it: T) = contains(it)

operator fun ByteString.plus(it: ByteString): ByteArray = toByteArray() + it.toByteArray()

tailrec fun Any?.toLog(): String = when (this) {
    is ByteArray -> toHexString()
    is Array<*>  -> toList().toLog()
    is List<*>   -> map { it.toLog() }.toString()
    else         -> toString()
}

fun Array<Any>.toLog() = toList().map { it.toLog() }

fun <T> List<T>.onNotEmpty(convert: (List<T>) -> Unit) {
    if (isNotEmpty()) convert(this)
}

fun <T> List<T>.ifNotEmpty(convert: (List<T>) -> List<T>): List<T> =
    if (isEmpty()) this else convert(this)

fun <T> List<T>.prepend(item: T) = listOf(item) + this
