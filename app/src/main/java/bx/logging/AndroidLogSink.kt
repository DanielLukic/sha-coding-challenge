package bx.logging

class AndroidLogSink : LogSink {

    override fun log(level: Log.Level, tag: String, message: String?, throwable: Throwable?) {
        when (level) {
            Log.Level.VERBOSE -> android.util.Log.v(tag, message, throwable)
            Log.Level.DEBUG   -> android.util.Log.d(tag, message, throwable)
            Log.Level.INFO    -> android.util.Log.i(tag, message, throwable)
            Log.Level.WARN    -> android.util.Log.w(tag, message, throwable)
            Log.Level.ERROR   -> android.util.Log.e(tag, message, throwable)
            Log.Level.NONE    -> Unit
        }
    }
}
