package bx.util

import bx.logging.Log
import bx.system.Concurrency
import bx.system.Disposable
import java.lang.ref.WeakReference

interface AutoDisposeBx {

    fun Disposable.autoDispose(tag: String)
    fun dispose(tag: String)
    fun dispose()

    companion object {
        operator fun invoke(concurrency: Concurrency = Concurrency()): AutoDisposeBx = object :
            AutoDisposeBx {

            private val disposables = mutableMapOf<String, WeakReference<Disposable>>()

            override fun Disposable.autoDispose(tag: String) {
                concurrency.ensureMainThread()
                Log.verbose { "autoDispose $tag | $disposables" }
                disposables[tag]?.get()?.dispose()
                disposables[tag] = WeakReference(this)
            }

            override fun dispose(tag: String) {
                concurrency.ensureMainThread()
                Log.verbose { "dispose $tag | $disposables" }
                disposables.remove(tag)?.get()?.dispose()
            }

            override fun dispose() {
                concurrency.ensureMainThread()
                Log.verbose { "dispose $disposables" }
                disposables.values.forEach { it.get()?.dispose() }
                disposables.clear()
            }
        }
    }
}
