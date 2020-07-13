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
) : DisposableMiddleware<Action, Action.Effect>() {

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

    override fun handle(action: Action) {
        when (action) {
            is Action.User.SearchQuery -> {
                searchQuery.offer(action.query)
            }
            is Action.User.SearchClear,
            is Action.User.SearchClose -> {
                searchJob?.cancel()
            }
        }
    }

    private suspend fun search(query: String) {
        if (query.isEmpty()) {
            effect(Action.Effect.SearchEmpty)
            return
        }
        effect(Action.Effect.SearchLoading)
        val result = newsInteractor.search(query)
        when (val mapped = result.map(null, NewsMapper::mapToNewsItems)) {
            is ContentResult.Success -> effect(Action.Effect.SearchSuccess(mapped.data))
            is ContentResult.Error -> effect(Action.Effect.SearchError)
        }
    }

}
