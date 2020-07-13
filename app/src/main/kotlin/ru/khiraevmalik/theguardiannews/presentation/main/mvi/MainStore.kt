package ru.khiraevmalik.theguardiannews.presentation.main.mvi

import ru.khiraevmalik.theguardiannews.mvi_no_reactive.Middleware
import ru.khiraevmalik.theguardiannews.mvi_no_reactive.Reducer
import ru.khiraevmalik.theguardiannews.mvi_no_reactive.Store

class MainStore(
        reducer: Reducer<State, Action>,
        middleware: List<Middleware<Action, Action.Effect>>
) : Store<Action, Action.User, Action.Effect, State>(
        reducer, middleware, initialState = State.Fetch.Loading
) {

    private var listener: (State) -> Unit = {}

    fun setOnStateUpdateListener(listener: (State) -> Unit) {
        this.listener = listener
    }

    override fun onStateUpdate(state: State) {
        listener.invoke(state)
    }

}
