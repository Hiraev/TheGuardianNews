package ru.khiraevmalik.theguardiannews.mvi_no_reactive

/**
 * Class handles action (A) and produces effects (E) and news (N)
 * Effects are also action, so Reduces can reduce it too.
 * News have to be consumed only one time or be passed
 *
 * Can be used to make some Network call.
 * effect() function may be call at any time, when result will be skipped
 *
 * To understand A, E, N @see [ru.khiraevmalik.theguardiannews.mvi_no_reactive.Store]
 */
abstract class Middleware<A, E, N> where E : A {

    private var effectListener: (E) -> Unit = {}
    private var newsListener: (N) -> Unit = {}

    abstract fun handle(action: A)
    open fun onDispose() {}

    fun effect(e: E) {
        effectListener.invoke(e)
    }

    fun event(e: N) {
        newsListener.invoke(e)
    }

    internal fun dispose() {
        effectListener = {}
        newsListener = {}
        onDispose()
    }

    internal fun setEffectListener(listener: (E) -> Unit) {
        effectListener = listener
    }

    internal fun setNewsListener(listener: (N) -> Unit) {
        newsListener = listener
    }

}
