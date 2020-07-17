package ru.khiraevmalik.theguardiannews.presentation.main

import ru.khiraevmalik.theguardiannews.mvi_base.CoroutinesMviViewModel
import ru.khiraevmalik.theguardiannews.presentation.main.adapter.NewsItem
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.Action
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.MainNews
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.MainStore
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.State
import ru.khiraevmalik.theguardiannews.routing.RootRouter

class MainNewsViewModel(
        store: MainStore,
        private val router: RootRouter
) : CoroutinesMviViewModel<Action, Action.User, Action.Effect, State, MainNews>(store) {

    init {
        proceed(Action.User.FetchNews)
    }

    fun openNewsView(newsItem: NewsItem.Data) {
        router.openNewsViewFragment(newsItem)
    }

}
