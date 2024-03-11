package bx.util

sealed class Optional<T> {

    companion object {
        fun <T> empty(): Empty<T> = Empty()
        fun <T : Any> of(it: T): Optional<T> = Value(it)
        fun <T : Any?> ofNullable(it: T?): Optional<T> = if (it != null) Value(it) else Empty()
    }

    val isEmpty get() = this is Empty
    val hasValue get() = this is Value

    fun <R : Any> ifValue(apply: (T) -> R): R? = valueOrNull()?.let(apply)

    fun valueOrNull() = (this as? Value)?.value

    fun onEmpty(block: () -> Unit): Optional<T> {
        if (this is Empty) block()
        return this
    }

    fun onValue(block: (T) -> Unit): Optional<T> {
        if (this is Value) block(value)
        return this
    }

    class Empty<T> : Optional<T>()

    data class Value<T>(val value: T) : Optional<T>()
}
