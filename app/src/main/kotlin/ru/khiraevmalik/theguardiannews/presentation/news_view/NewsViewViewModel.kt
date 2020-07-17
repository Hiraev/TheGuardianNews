package ru.khiraevmalik.theguardiannews.presentation.news_view

import ru.khiraevmalik.theguardiannews.CoroutinesViewModel
import ru.khiraevmalik.theguardiannews.routing.RootRouter

class NewsViewViewModel(
        private val router: RootRouter
) : CoroutinesViewModel() {

    fun back() {
        router.back()
    }

}
