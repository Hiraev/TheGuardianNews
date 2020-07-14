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
        launch {
            effect(Action.Effect.FetchLoading)
            val result = newsInteractor.loadNews()
            when (val mapped = result.map(null, NewsMapper::mapToNewsItems)) {
                is ContentResult.Success -> effect(Action.Effect.FetchSuccess(mapped.data))
                is ContentResult.Error -> effect(Action.Effect.FetchError)
            }
        }
    }

}
