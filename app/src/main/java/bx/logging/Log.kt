package bx.logging

import bx.logging.Log.error
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.UndeclaredThrowableException
import java.util.Arrays
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Suppress("MemberVisibilityCanBePrivate")
object Log {

    private val NO_MESSAGE = ""
    private val NOT_FOUND = StackTraceElement(NO_MESSAGE, NO_MESSAGE, NO_MESSAGE, 0)

    var tag = "LOG"
    var level = Level.DEBUG
    var traceLevel = Level.VERBOSE
    var sink: LogSink = SystemOutLogSink()
    var mode: Mode? = Mode.ENHANCED
    var twoLines = false

    enum class Level(val value: Int, val tag: String?) {
        NONE(0, null),
        ERROR(1, "E"),
        WARN(2, "W"),
        INFO(3, "I"),
        DEBUG(4, "D"),
        VERBOSE(5, "V"),
    }

    enum class Mode {
        BRIEF,
        ENHANCED,
        FULL
    }

    @Suppress("unused")
    fun <T> briefly(callable: Callable<T>): T {
        val savedMode = mode
        try {
            mode = Mode.BRIEF
            return callable.call()
        }
        finally {
            mode = savedMode
        }
    }

    fun `is`(level: Level): Boolean = level.value <= Log.level.value

    fun thread() {
        info(Thread.currentThread().toString())
    }

    fun verifyThread(prefix: String) {
        if (Thread.currentThread().name.startsWith(prefix)) return
        thread()
        error(Thread.currentThread().name + " != " + prefix + "...")
        stackTrace()
    }

    fun trace() {
        if (level.value < traceLevel.value) return
        val suffix = if (mode == Mode.FULL) "" else "#" + determineCaller().methodName
        sink.log(traceLevel, tag, enhanced("TRACE$suffix"), null)
    }

    /**
     * Use this to INFO level log a stack trace of the current execution point. Useful for locating spurious, duplicate,
     * unexpected calls to functions.
     */
    fun stackTrace() {
        val writer = StringWriter()
        val exception = RuntimeException()
        exception.printStackTrace(PrintWriter(writer))
        info(writer.toString().replace("java.lang.RuntimeException", "STACK TRACE"))
    }

    /**
     * Use this to fail the app hard right away. Consider not using this at all outside `if DEBUG` or comparable.
     */
    fun fail(message: String, vararg parameters: Any) {
        fail(null, message, *parameters)
    }

    /**
     * Use this to fail the app hard right away. Consider not using this at all outside `if DEBUG` or comparable.
     */
    fun fail(optionalThrowable: Throwable?, message: String, vararg parameters: Any) {
        error(optionalThrowable, message, *parameters)
        exitProcess(10)
    }

    fun error(throwable: Throwable) {
        if (level.value < Level.ERROR.value) return
        if (throwable is UnsupportedOperationException) error(throwable.toString())
        else error(throwable, (throwable.message ?: NO_MESSAGE).ifBlank { throwable.toString() })
    }

    fun error(message: String, vararg parameters: Any) {
        error(null, message, *parameters)
    }

    @Suppress("CascadeIf")
    tailrec fun error(throwable: Throwable?, message: String, vararg parameters: Any) {
        if (level.value < Level.ERROR.value) return
        if (throwable is UndeclaredThrowableException)
            error(throwable.undeclaredThrowable, message, parameters)
        else if (throwable is InvocationTargetException)
            error(throwable.targetException, message, parameters)
        else
            sink.log(Level.ERROR, tag, enhanced(formatted(message, *parameters)), throwable)
    }

    /**
     * Use this to create a pseudo crash for explicit tracking with trending in crashlytics. Use sparingly. In contrast,
     * [error] will only write an `E` log entry line without stack trace, tracking and trending.
     */
    fun trackedError(message: String, vararg parameters: Any) {
        if (level.value < Level.ERROR.value) return
        val it = enhanced(formatted(message, *parameters))
        sink.log(Level.ERROR, tag, it, java.lang.RuntimeException(it))
    }

    fun warn(message: String, vararg parameters: Any) {
        if (level.value < Level.WARN.value) return
        sink.log(Level.WARN, tag, enhanced(formatted(message, *parameters)), null)
    }

    fun info(message: String, vararg parameters: Any) {
        if (level.value < Level.INFO.value) return
        sink.log(Level.INFO, tag, enhanced(formatted(message, *parameters)), null)
    }

    fun debug(message: String, vararg parameters: Any) {
        if (level.value < Level.DEBUG.value) return
        sink.log(Level.DEBUG, tag, enhanced(formatted(message, *parameters)), null)
    }

    fun verbose(message: String, vararg parameters: Any) {
        if (level.value < Level.VERBOSE.value) return
        sink.log(Level.VERBOSE, tag, enhanced(formatted(message, *parameters)), null)
    }

    fun log(level: Level, message: String, vararg parameters: Any) {
        if (Log.level.value < level.value) return
        sink.log(level, tag, enhanced(formatted(message, *parameters)), null)
    }

    private fun enhanced(message: String): String {
        if (mode == Mode.BRIEF || mode == null) return message
        val caller = determineCaller()
        val classNameOnly = getClassNameOnly(caller.className)
        val methodName = getMethodName(caller)
        val lineNumber = caller.lineNumber
        val threadInfo = threadInfo()
        // Note the . before the opening parenthesis. It is required to make IntelliJ create a clickable link.
        val separator = if (twoLines) "\n" else " "
        return "$message$separator$threadInfo$methodName.($classNameOnly.java:$lineNumber)"
    }

    private fun getMethodName(caller: StackTraceElement): String {
        if (mode != Mode.FULL) return ""
        val result = caller.methodName
        return if (result.startsWith("lambda\$iterator$")) "" else String.format("[%s] ", result)
    }

    private fun determineCaller(): StackTraceElement {
        for (element in RuntimeException().stackTrace) {
            if (element.className.startsWith("bx.logging.")) continue
            if (element.className.contains("WuQiLogHelper")) continue
            return element
        }
        return NOT_FOUND
    }

    private fun getClassNameOnly(classNameWithPackage: String): String {
        val lastDotPos = classNameWithPackage.lastIndexOf('.')
        return if (lastDotPos == -1) classNameWithPackage
        else classNameWithPackage.substring(lastDotPos + 1)
            .replaceFirst("\\$.*".toRegex(), "")
    }

    private fun threadInfo(): String {
        val thread = Thread.currentThread()
        if (thread.name == "main") return ""
        if (thread.name.startsWith("OkHttp ")) return "[OkHttp] "
        val result = if (thread.threadGroup?.name == "main") "[${thread.name}] " else "$thread "
        return result.replace("RxCachedThreadScheduler", "RxCached")
    }

    private fun formatted(message: String, vararg parameters: Any) = try {
        if (parameters.isEmpty()) message else String.format(message, *parameters)
    }
    catch (throwable: Throwable) {
        error(
            throwable,
            "failed formatting log message - ignored: %s with %s",
            message,
            Arrays.asList(*parameters)
        )
        message
    }

    fun debug(message: () -> Any?) {
        if (level < Level.DEBUG) return
        debug(message().toString())
    }

    fun verbose(message: () -> Any?) {
        if (level < Level.VERBOSE) return
        verbose(message().toString())
    }

    fun info(message: () -> Any?) {
        if (level < Level.INFO) return
        info(message().toString())
    }

    fun warn(message: () -> Any?) {
        if (level < Level.WARN) return
        warn(message().toString())
    }

    fun error(failure: Throwable? = null, message: () -> Any? = { "" }) {
        if (level < Level.ERROR) return
        error(failure, message().toString())
    }

    /**
     * Use this to create a pseudo crash for explicit tracking with trending in crashlytics. Use sparingly. In contrast,
     * [error] will only write an `E` log entry line without stack trace, tracking and trending.
     */
    fun trackedError(message: () -> Any? = { "" }) {
        if (level < Level.ERROR) return
        trackedError(message().toString())
    }

}
