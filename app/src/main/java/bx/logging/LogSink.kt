package bx.logging

interface LogSink {
    fun log(level: Log.Level, tag: String, message: String?, throwable: Throwable?)
}
