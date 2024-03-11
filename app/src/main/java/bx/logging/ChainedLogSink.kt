package bx.logging

import java.util.ArrayList

class ChainedLogSink : LogSink {

    private val logSinks = ArrayList<LogSink>()

    fun add(logSink: LogSink): ChainedLogSink {
        logSinks.add(logSink)
        return this
    }

    override fun log(level: Log.Level, tag: String, message: String?, throwable: Throwable?) {
        logSinks.forEach { it.log(level, tag, message, throwable) }
    }
}
