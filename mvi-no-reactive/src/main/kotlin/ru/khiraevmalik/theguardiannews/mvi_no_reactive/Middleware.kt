package ru.khiraevmalik.theguardiannews.mvi_no_reactive

/**
 * Handle action and produces effects
 */
abstract class Middleware<A, E> where E : A {

    private var effectListener: (E) -> Unit = {}

    abstract fun dispose()
    abstract fun handle(action: A)

    fun effect(e: E) {
        effectListener.invoke(e)
    }

    internal fun setEffectListener(listener: (E) -> Unit) {
        effectListener = listener
    }

}
