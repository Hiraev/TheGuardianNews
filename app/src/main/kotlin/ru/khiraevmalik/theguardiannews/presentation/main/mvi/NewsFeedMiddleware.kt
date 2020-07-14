package ru.khiraevmalik.theguardiannews.presentation.main.mvi

import ru.khiraevmalik.theguardiannews.ContentResult
import ru.khiraevmalik.theguardiannews.main.NewsInteractor
import ru.khiraevmalik.theguardiannews.mappers.NewsMapper
import ru.khiraevmalik.theguardiannews.mappers.map
import ru.khiraevmalik.theguardiannews.mvi_base.DisposableMiddleware

class NewsFeedMiddleware(
        private val newsInteractor: NewsInteractor
) : DisposableMiddleware<Action, Action.Effect, MviNews>() {

    override fun handle(action: Action) {
        when (action) {
            is Action.User.FetchNews -> fetchNews()
        }
    }

    private fun fetchNews() {
        effectOnMain(Action.Effect.FetchLoading)
        launch {
            val result = newsInteractor.loadNews()
            when (val mapped = result.map(null, NewsMapper::mapToNewsItems)) {
                is ContentResult.Success -> effectOnMain(Action.Effect.FetchSuccess(mapped.data))
                is ContentResult.Error -> effectOnMain(Action.Effect.FetchError)
            }
        }
    }

}
