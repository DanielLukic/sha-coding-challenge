package bx.system

interface Disposable {
    fun dispose()

    companion object {
        val DISPOSED = object : Disposable {
            override fun dispose() {}
        }
    }
}
