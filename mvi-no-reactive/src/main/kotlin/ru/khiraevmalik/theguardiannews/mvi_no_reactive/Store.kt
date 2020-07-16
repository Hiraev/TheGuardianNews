package ru.khiraevmalik.theguardiannews.mvi_no_reactive

import android.os.Looper

/**
 * This class presents the MVI pattern.
 * You have to call act function to send action,
 * and set onStateChangeListener to get state
 * changes.
 *
 * Use news (N) to get events that won't be saved
 * They have been consumed only one time, or be skipped
 *
 * A - action
 * U - user action
 * E - effect action
 * S - state
 * N - news
 *
 * Example:
 *
 * sealed class A {
 *      sealed class U: A() {
 *          object Click: U()
 *      }
 *      sealed class E: A() {
 *          object ResponseError: E()
 *          class ResponseSuccess(val response: Boolean): E()
 *      }
 * }
 */
abstract class Store<A, U, E, S, N>(
        private val reducer: Reducer<S, A>,
        private val middleware: List<Middleware<A, E, S, N>>,
        initialState: S
) where U : A, E : A {

    init {
        middleware.forEach {
            it.setEffectListener(::onEffect)
        }
        middleware.forEach {
            it.setNewsListener(::onEvent)
        }
    }

    private var onStateChangeListener: (S) -> Unit = {}
    private var onNewsListener: (N) -> Unit = {}

    private var state: S = initialState
        set(value) {
            field = value
            onStateChangeListener.invoke(value)
        }

    fun setOnStateChangeListener(onStateChangeListener: (S) -> Unit) {
        this.onStateChangeListener = onStateChangeListener
    }

    fun setEventsListener(onEventListener: (N) -> Unit) {
        this.onNewsListener = onEventListener
    }

    fun proceed(action: A) {
        if (Looper.myLooper() != Looper.getMainLooper()) throw IllegalStateException("proceed function must be called on main thread")
        val newState = reducer.reduce(action, state)
        middleware.forEach { m -> m.handle(action, state) }
        onProceed(action, state, newState)
        state = newState
    }

    fun dispose() {
        onStateChangeListener = {}
        onNewsListener = {}
        middleware.forEach(Middleware<A, E, S, N>::dispose)
    }

    open fun onProceed(action: A, oldState: S, newState: S) {}

    private fun onEffect(effect: E) {
        proceed(effect)
    }

    private fun onEvent(event: N) {
        onNewsListener.invoke(event)
    }

}
