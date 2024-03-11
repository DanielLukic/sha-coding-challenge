@file:Suppress("UNCHECKED_CAST")

package bx.system

import android.content.SharedPreferences
import bx.logging.Log

interface Preferences {

    fun attach(it: Listener): Boolean
    fun detach(it: Listener): Boolean

    fun contains(key: String): Boolean
    fun remove(key: String)

    operator fun <T> get(key: String): T?
    operator fun <T> get(key: String, default: Any): T = (get(key) ?: default) as T

    operator fun set(key: String, newValue: String)
    operator fun set(key: String, newValue: Float)
    operator fun set(key: String, newValue: Boolean)

    interface Listener {
        fun onChanged(key: String, newValue: Any?)
    }

    companion object {

        operator fun invoke(preferences: SharedPreferences): Preferences = object : Preferences {

            private val listeners: MutableSet<Listener> = mutableSetOf()

            override fun attach(it: Listener) = listeners.add(it)
            override fun detach(it: Listener) = listeners.remove(it)

            override fun contains(key: String): Boolean = preferences.contains(key)
            override fun remove(key: String) = preferences.edit().remove(key).apply()

            override operator fun <T> get(key: String): T? = preferences.all[key] as T?

            override operator fun set(key: String, newValue: String) {
                Log.verbose { "set $key: $newValue" }
                preferences.edit().putString(key, newValue).apply()
                listeners.forEach { it.onChanged(key, newValue) }
            }

            override fun set(key: String, newValue: Float) {
                Log.verbose { "set $key: $newValue" }
                preferences.edit().putFloat(key, newValue).apply()
                listeners.forEach { it.onChanged(key, newValue) }
            }

            override operator fun set(key: String, newValue: Boolean) {
                Log.verbose { "set $key: $newValue" }
                preferences.edit().putBoolean(key, newValue).apply()
                listeners.forEach { it.onChanged(key, newValue) }
            }
        }
    }
}
