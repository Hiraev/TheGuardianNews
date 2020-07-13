package ru.khiraevmalik.theguardiannews.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import ru.khiraevmalik.theguardiannews.CoroutinesViewModel
import ru.khiraevmalik.theguardiannews.Result
import ru.khiraevmalik.theguardiannews.main.NewsInteractor
import ru.khiraevmalik.theguardiannews.mappers.NewsMapper
import ru.khiraevmalik.theguardiannews.mappers.map
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.Action
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.State

class MainNewsViewModel(
        private val newsInteractor: NewsInteractor
) : CoroutinesViewModel() {

    private var lastFetchState: State.Fetch = State.Fetch.Loading
    private var lastSearchState: State.Search = State.Search.Idle
    private var state: State = lastFetchState
        set(value) {
            field = value
            mStateLiveData.value = value
        }

    private val mStateLiveData = MutableLiveData<State>(lastFetchState)

    private val searchQuery = Channel<String>(Channel.CONFLATED)

    val stateLiveData: LiveData<State> = mStateLiveData

    companion object {
        private const val SEARCH_QUERY_DEBOUNCE = 500L
    }

    init {
        launch {
            searchQuery.receiveAsFlow()
                    .distinctUntilChanged()
                    .collectLatest { query ->
                        delay(SEARCH_QUERY_DEBOUNCE)
                        newsInteractor.cancelSearch()
                        act(searchQuery(query))
                    }
        }
    }

    fun act(action: Action) {
        val newState = reduce(action, state)
        val effect = middleware(action)
        when (val lastState = internalReduce(action, state)) {
            is State.Fetch -> lastFetchState = lastState
            is State.Search -> lastSearchState = lastState
        }
        Log.i("ACTION/STATE", "[${action::class.simpleName}] (${newState::class.simpleName})")
        state = newState
        // Apply effect
        if (effect !is Action.Effect.Nope) act(effect)
    }

    private fun reduce(action: Action, state: State) = when (state) {
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

    private fun internalReduce(action: Action, state: State) = when (action) {
        is Action.User.SearchClose -> lastFetchState
        is Action.User.SearchClear -> State.Search.EmptyQuery
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

    private fun middleware(action: Action): Action.Effect = when (action) {
        is Action.User.FetchNews -> {
            loadNews()
        }
        is Action.User.SearchQuery -> {
            searchQuery.offer(action.query)
            Action.Effect.Nope
        }
        else -> Action.Effect.Nope
    }

    private fun loadNews(): Action.Effect {
        launch {
            val result = newsInteractor
                    .loadNews()
                    .map(null, NewsMapper::mapToNewsItems)
            val effect = when (result) {
                is Result.Success -> Action.Effect.FetchSuccess(result.data)
                is Result.Error -> Action.Effect.FetchError
            }
            act(effect)
        }
        return Action.Effect.FetchLoading
    }

    private fun searchQuery(query: String) = if (query.isEmpty()) {
        Action.Effect.SearchEmpty
    } else {
        launch {
            val effect = when (val result = newsInteractor.search(query)) {
                is Result.Error -> {
                    Action.Effect.SearchError
                }
                is Result.Success -> {
                    Action.Effect.SearchSuccess(NewsMapper.mapToNewsItems(result.data))
                }
            }
            act(effect)
        }
        Action.Effect.SearchLoading
    }

}
