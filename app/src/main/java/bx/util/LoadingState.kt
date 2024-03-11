package bx.util

data class LoadingState<T>(
    val value: T? = null,
    val error: Throwable? = null,
    val loading: Boolean = false
)
