package bx.system

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object Cx {
    var Default: CoroutineDispatcher = Dispatchers.Default
    var IO: CoroutineDispatcher = Dispatchers.IO
    var Main: CoroutineDispatcher = Dispatchers.Main
    var MainImmediate: CoroutineDispatcher = Dispatchers.Main.immediate
    var Unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}
