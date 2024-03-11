package bx.extension

import bx.logging.Log
import java.lang.reflect.Method
import java.lang.reflect.Proxy

inline fun <reified TARGET : Any, SOURCE : Any> duckTyped(source: SOURCE): TARGET {

    TARGET::class.java.declaredMethods.forEach { method ->
        val candidate = source.javaClass.declaredMethods.find { it.name == method.name }
            ?: error("missing $method on $source")

        if (candidate.parameterTypes.size != method.parameterTypes.size) {
            error("parameter count mismatch: $method on $source")
        }
        candidate.parameterTypes.forEachIndexed { index, type ->
            if (method.parameterTypes[index] != type) {
                error("parameter type mismatch: $method on $source")
            }
        }
        if (candidate.returnType != method.returnType) {
            error("return type mismatch: $method on $source")
        }
    }

    val invocation = { _: Any, method: Method, _arguments: Array<Any>? ->

        val arguments = _arguments ?: emptyArray()

        Log.verbose { "invoke $method on $source with ${arguments.toList()}" }
        val candidate = source.javaClass.declaredMethods.filter { it.name == method.name }

        Log.verbose { "seeing $candidate" }
        val it = candidate.find { it.parameterTypes.size == arguments.size }
            ?: error("method mismatch: $method not found on $source")

        it.isAccessible = true
        it.invoke(source, *arguments)
    }
    return Proxy.newProxyInstance(source.javaClass.classLoader, arrayOf(TARGET::class.java), invocation) as TARGET
}
