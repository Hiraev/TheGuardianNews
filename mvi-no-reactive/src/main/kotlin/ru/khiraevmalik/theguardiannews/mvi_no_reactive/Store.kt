package ru.khiraevmalik.theguardiannews.mvi_no_reactive

/**
 * This class presents the MVI pattern.
 * You have to call act function to send action,
 * and set onStateChangeListener to get state
 * changes.
 *
 * A - action
 * U - user action
 * E - effect action
 * S - state
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
abstract class Store<A, U, E, S>(
        private val reducer: Reducer<S, A>,
        private val middleware: List<Middleware<A, E>>,
        initialState: S
) where U : A, E : A {

    init {
        middleware.forEach {
            it.setEffectListener(::onEffect)
        }
    }

    private var onStateChangeListener: (S) -> Unit = {}

    private var state: S = initialState
        set(value) {
            field = value
            onStateChangeListener.invoke(value)
        }

    fun setOnStateChangeListener(onStateChangeListener: (S) -> Unit) {
        this.onStateChangeListener = onStateChangeListener
    }

    fun act(action: A) {
        val newState = reducer.reduce(action, state)
        middleware.forEach { m -> m.handle(action) }
        state = newState
    }

    fun dispose() {
        onStateChangeListener = {}
        middleware.forEach(Middleware<A, E>::dispose)
    }

    private fun onEffect(effect: E) {
        act(effect)
    }

}
