package ru.khiraevmalik.theguardiannews.presentation.main.mvi

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import ru.khiraevmalik.theguardiannews.ContentResult
import ru.khiraevmalik.theguardiannews.main.NewsInteractor
import ru.khiraevmalik.theguardiannews.mappers.NewsMapper
import ru.khiraevmalik.theguardiannews.mappers.map
import ru.khiraevmalik.theguardiannews.mvi_base.DisposableMiddleware

class SearchMiddleware(
        private val newsInteractor: NewsInteractor
) : DisposableMiddleware<Action, Action.Effect, State, MainNews>() {

    private var searchJob: Job? = null

    private val searchQuery = Channel<String>(Channel.CONFLATED)

    companion object {
        private const val SEARCH_QUERY_DEBOUNCE = 500L
    }

    init {
        launch {
            searchQuery.receiveAsFlow()
                    .distinctUntilChanged()
                    .collectLatest { query ->
                        delay(SEARCH_QUERY_DEBOUNCE)
                        searchJob?.cancel()
                        searchJob = launch {
                            search(query)
                        }
                    }
        }
    }

    override fun handle(action: Action, state: State) {
        when (action) {
            is Action.User.Retry -> {
                if (state is State.Search.Error) search(state.lastQuery)
            }
            is Action.User.SearchQuery -> {
                searchQuery.offer(action.query)
            }
            is Action.User.SearchClear -> {
                news(MainNews.Search.ClearSearchEditText)
                searchJob?.cancel()
            }
            is Action.User.SearchClose -> {
                effectOnMain(Action.Effect.SearchClose)
                searchJob?.cancel()
            }
        }
    }

    private fun search(query: String) {
        if (query.isEmpty()) {
            effectOnMain(Action.Effect.SearchEmpty)
            return
        }
        effectOnMain(Action.Effect.SearchLoading)
        launch {
            val result = newsInteractor.search(query)
            when (val mapped = result.map(null, NewsMapper::mapToNewsItems)) {
                is ContentResult.Success -> effectOnMain(Action.Effect.SearchSuccess(mapped.data))
                is ContentResult.Error -> effectOnMain(Action.Effect.SearchError(query))
            }
        }
    }

}
