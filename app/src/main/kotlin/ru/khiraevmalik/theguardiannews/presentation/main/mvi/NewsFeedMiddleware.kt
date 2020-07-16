package ru.khiraevmalik.theguardiannews.presentation.main.mvi

import kotlinx.coroutines.Job
import ru.khiraevmalik.theguardiannews.ContentResult
import ru.khiraevmalik.theguardiannews.base.PagingStatus
import ru.khiraevmalik.theguardiannews.main.NewsInteractor
import ru.khiraevmalik.theguardiannews.mappers.NewsMapper
import ru.khiraevmalik.theguardiannews.mappers.map
import ru.khiraevmalik.theguardiannews.mvi_base.DisposableMiddleware
import ru.khiraevmalik.theguardiannews.presentation.main.adapter.NewsItem
import java.util.concurrent.atomic.AtomicBoolean

class NewsFeedMiddleware(
        private val newsInteractor: NewsInteractor
) : DisposableMiddleware<Action, Action.Effect, State, MainNews>() {

    private var pageNumber = START_PAGE_NUMBER
    private var loadingModeJob: Job? = null
    private var hasMoreData = AtomicBoolean(true)

    companion object {
        private const val START_PAGE_NUMBER = 1
        private const val PAGE_SIZE = 20
    }

    override fun handle(action: Action, state: State) {
        when (action) {
            is Action.User.Retry -> handleRetry(state)
            is Action.User.FetchNews -> fetchNews()
            // Extract old data from state and load more
            is Action.User.FetchMore -> if (state is State.Fetch.Success) fetchMoreNews(state.news)
        }
    }

    private fun handleRetry(state: State) {
        when (state) {
            is State.Fetch.Error -> fetchNews()
            is State.Fetch.Success -> fetchMoreNews(state.news)
        }
    }

    private fun fetchNews() {
        effectOnMain(Action.Effect.FetchLoading)
        launch {
            val result = newsInteractor.loadNews(START_PAGE_NUMBER, PAGE_SIZE)
            when (val mapped = result.map(null, NewsMapper::mapToNewsItems)) {
                is ContentResult.Success -> effectOnMain(Action.Effect.FetchSuccess(mapped.data, PagingStatus.HAS_MORE))
                is ContentResult.Error -> effectOnMain(Action.Effect.FetchError)
            }
        }
    }

    private fun fetchMoreNews(old: List<NewsItem>) {
        if (needToPassFetching()) return
        loadingModeJob = launch {
            effectOnMain(Action.Effect.FetchSuccess(old, PagingStatus.LOADING))
            val result = newsInteractor.loadNews(pageNumber + 1, PAGE_SIZE)
            when (val mapped = result.map(null, NewsMapper::mapToNewsItems)) {
                is ContentResult.Success -> handleSuccessfullyFetchedMoreData(old, mapped.data)
                is ContentResult.Error -> effectOnMain(Action.Effect.FetchSuccess(old, PagingStatus.ERROR))
            }
        }
    }

    private fun handleSuccessfullyFetchedMoreData(old: List<NewsItem>, new: List<NewsItem>) {
        if (isLastPage(new)) {
            hasMoreData.set(false)
            pageNumber = START_PAGE_NUMBER
            effectOnMain(Action.Effect.FetchSuccess(old + new, PagingStatus.FULL))
        } else {
            pageNumber++
            effectOnMain(Action.Effect.FetchSuccess(old + new, PagingStatus.HAS_MORE))
        }
    }

    private fun needToPassFetching() = !hasMoreData.get() || loadingModeJob?.isActive == true

    private fun isLastPage(news: List<NewsItem>) = news.isEmpty() || news.size < PAGE_SIZE

}
