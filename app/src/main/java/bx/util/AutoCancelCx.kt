package bx.util

import bx.logging.Log
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren

interface AutoCancelCx : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = scope.coroutineContext

    val scope: CoroutineScope
    fun cancelJob(tag: String)
    fun Job.autoCancel(tag: String)
    fun cancelChildren() = scope.coroutineContext.cancelChildren()
    fun cancel() = scope.cancel()

    companion object {
        operator fun invoke(name: String = "AutoCancelCx") = invoke(SafeMainScope(name))
        operator fun invoke(scope: CoroutineScope): AutoCancelCx = object : AutoCancelCx {
            private val autoCancelJobs = mutableMapOf<String, Job>()
            override val scope = scope

            override fun cancelJob(tag: String) {
                val job = autoCancelJobs.remove(tag) ?: return
                if (job.isCompleted) return
                if (job.isCancelled) return
                job.cancel()
                Log.verbose { "explicitly cancelled $tag" }
            }

            override fun Job.autoCancel(tag: String) {
                autoCancelJobs[tag]?.let {
                    if (!it.isCancelled && !it.isCompleted) it.cancel("auto canceled")
                }
                autoCancelJobs[tag] = this
            }
        }
    }
}
