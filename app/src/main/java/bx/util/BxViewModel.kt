package bx.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Convenience base class for mutable view models.
 *
 * Rationale: Hides the live data instance to not expose two objects to the associated view. The view should talk
 * only to its view model. Whether a live data object exists or not is an implementation detail.
 */
@Suppress("unused")
abstract class BxViewModel<T>(
    initial: T,
    name: String = "UnnamedViewModel",
    immediate: Boolean = true
) :
    ViewModel(), CoroutineScope,
    AutoCancelCx by AutoCancelCx(SafeViewModelScope(name, immediate)) {

    protected val data: MutableStateFlow<T> = MutableStateFlow(initial)

    protected fun <T> CoroutineScope.observe(
        data: Flow<T>,
        owner: LifecycleOwner,
        observer: (T) -> Unit
    ) {
        owner.lifecycle.addObserver(object : DefaultLifecycleObserver {

            var collector: Job? = null

            override fun onStart(owner: LifecycleOwner) {
                collector?.cancel()
                collector = launch { data.collect { observer(it) } }
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                collector?.cancel()
            }
        })
    }

    fun observeState() = data.asStateFlow()

    fun observe(owner: LifecycleOwner, observer: Observer<T>) = observe(owner, observer::onChanged)
    fun observe(owner: LifecycleOwner, observer: (T) -> Unit) = observe(data, owner, observer)

    protected fun update(force: Boolean = false, block: T.() -> T) {
        val input = data.value
        val update = block(input)
        if (force || input != update) {
            data.update { update }
            onChange(update)
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected open fun onChange(it: T) = Unit

    override val coroutineContext: CoroutineContext get() = scope.coroutineContext

    override fun onCleared() {
        super.onCleared()
        cancel()
    }

}
