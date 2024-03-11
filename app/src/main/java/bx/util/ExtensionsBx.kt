package bx.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import bx.system.Disposable

@Deprecated("Use AutoDisposeBx instead")
fun Disposable.autoDisposeOnPause(lifecycle: Lifecycle): Disposable {
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_PAUSE) dispose()
        }
    })
    return this
}
