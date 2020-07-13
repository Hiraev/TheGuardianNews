package ru.khiraevmalik.theguardiannews.mvi_no_reactive

/**
 * Class handles action (A) and produces effects (E)
 * Effects are also action, so Reduces can reduce it too.
 *
 * Can be used to make some Network call.
 * effect() function may be call at any time, when result will be ready
 *
 * To understand A, E @see [ru.khiraevmalik.theguardiannews.mvi_no_reactive.Store]
 */
abstract class Middleware<A, E> where E : A {

    private var effectListener: (E) -> Unit = {}

    abstract fun handle(action: A)
    open fun onDispose() {}

    fun effect(e: E) {
        effectListener.invoke(e)
    }

    internal fun dispose() {
        effectListener = {}
        onDispose()
    }

    internal fun setEffectListener(listener: (E) -> Unit) {
        effectListener = listener
    }

}
