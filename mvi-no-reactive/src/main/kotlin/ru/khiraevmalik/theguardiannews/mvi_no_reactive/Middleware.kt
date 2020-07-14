package ru.khiraevmalik.theguardiannews.mvi_no_reactive

import android.os.Looper

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
abstract class Middleware<A, E, S, N> where E : A {

    private var effectListener: (E) -> Unit = {}
    private var newsListener: (N) -> Unit = {}

    abstract fun handle(action: A, state: S)
    open fun onDispose() {}

    /**
     * Effect function must be called on main thread
     * @see [ru.khiraevmalik.theguardiannews.mvi_no_reactive.Store.proceed]
     */
    fun effect(effect: E) {
        effectListener.invoke(effect)
    }

    /**
     * News function must be called on main thread
     * @see [ru.khiraevmalik.theguardiannews.mvi_no_reactive.Store.onEvent]
     */
    fun news(news: N) {
        if (Looper.myLooper() != Looper.getMainLooper()) throw IllegalStateException("news function must be called on main thread")
        newsListener.invoke(news)
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
