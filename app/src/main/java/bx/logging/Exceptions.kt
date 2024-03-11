package bx.logging

inline fun <reified T : Throwable> Throwable.isOrHasCause() = isOrHasCause(T::class.java)

tailrec fun <T : Throwable> Throwable.isOrHasCause(type: Class<T>): Boolean {
    if (type.isAssignableFrom(javaClass)) return true
    val causeOrNull = cause ?: return false
    return causeOrNull.isOrHasCause(type)
}

val Throwable?.rootCause: Throwable?
    get() {
        if (this == null) return null
        val causeOrNull = cause ?: return this
        return causeOrNull.rootCause
    }
