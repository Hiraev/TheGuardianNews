package ru.khiraevmalik.theguardiannews.presentation.main.mvi

import ru.khiraevmalik.theguardiannews.mvi_no_reactive.Reducer

class MainReducer : Reducer<State, Action> {

    private var lastFetchState: State.Fetch = State.Fetch.Loading
    private var lastSearchState: State.Search = State.Search.Idle

    override fun reduce(action: Action, state: State): State {
        when (val lastState = internalReduce(action, state)) {
            is State.Fetch -> lastFetchState = lastState
            is State.Search -> lastSearchState = lastState
        }
        return when (state) {
            is State.Search -> when (action) {
                is Action.User.SearchClose,
                is Action.User.SearchClear,
                is Action.Effect.SearchEmpty,
                is Action.Effect.SearchLoading,
                is Action.Effect.SearchError,
                is Action.Effect.SearchSuccess -> internalReduce(action, state)
                else -> state
            }
            is State.Fetch -> when (action) {
                is Action.User.SearchOpen,
                is Action.Effect.FetchError,
                is Action.Effect.FetchLoading,
                is Action.Effect.FetchSuccess -> internalReduce(action, state)
                else -> state
            }
        }
    }

    private fun internalReduce(action: Action, state: State): State = when (action) {
        is Action.User.SearchClose -> lastFetchState
        is Action.User.SearchClear,
        is Action.Effect.SearchEmpty -> State.Search.Idle
        is Action.Effect.SearchLoading -> State.Search.Loading
        is Action.Effect.SearchError -> State.Search.Error
        is Action.Effect.SearchSuccess -> State.Search.Success(action.news)
        is Action.User.SearchOpen -> lastSearchState
        is Action.Effect.FetchError -> State.Fetch.Error
        is Action.Effect.FetchLoading -> State.Fetch.Loading
        is Action.Effect.FetchSuccess -> State.Fetch.Success(action.news)
        else -> state
    }

}
