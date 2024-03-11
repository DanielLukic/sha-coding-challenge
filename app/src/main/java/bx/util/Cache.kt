package bx.util

import java.util.concurrent.ConcurrentHashMap

interface Cache {

    fun clear()
    fun <T> remove(key: String): T?
    fun <T> set(key: String, it: T): T?
    fun <T> get(key: String): T?
    fun has(key: String): Boolean

    companion object {
        operator fun invoke() = object : Cache {
            private val data = ConcurrentHashMap<String, Any>()
            override fun clear() = data.clear()
            override fun <T> remove(key: String): T? = data.remove(key) as T?
            override fun <T> set(key: String, it: T): T? = data.put(key, it as Any) as? T?
            override fun <T> get(key: String): T? = data.get(key) as? T?
            override fun has(key: String): Boolean = data.containsKey(key)
        }
    }
}
