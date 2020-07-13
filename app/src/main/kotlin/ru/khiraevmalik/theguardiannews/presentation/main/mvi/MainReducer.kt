package ru.khiraevmalik.theguardiannews.presentation.main.mvi

import ru.khiraevmalik.theguardiannews.mvi_no_reactive.Reducer

class MainReducer : Reducer<State, Action> {

    private var lastFetchState: State.Fetch = State.Fetch.Loading
    private var lastSearchState: State.Search = State.Search.Idle

    override fun reduce(action: Action, state: State): State {
        val result = when (state) {
            is State.Search -> when (action) {
                is Action.User.SearchClose -> lastFetchState
                is Action.User.SearchClear -> State.Search.EmptyQuery
                is Action.Effect.SearchEmpty -> State.Search.Idle
                is Action.Effect.SearchLoading -> State.Search.Loading
                is Action.Effect.SearchError -> State.Search.Error
                is Action.Effect.SearchSuccess -> State.Search.Success(action.news)
                else -> state
            }
            is State.Fetch -> when (action) {
                is Action.User.SearchOpen -> lastSearchState
                is Action.Effect.FetchError -> State.Fetch.Error
                is Action.Effect.FetchLoading -> State.Fetch.Loading
                is Action.Effect.FetchSuccess -> State.Fetch.Success(action.news)
                else -> state
            }
        }
        when (result) {
            is State.Fetch -> lastFetchState = result
            is State.Search -> lastSearchState = result
        }
        return result
    }

}
