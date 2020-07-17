package ru.khiraevmalik.theguardiannews.routing

import ru.khiraevmalik.theguardiannews.presentation.main.adapter.NewsItem
import ru.terrakok.cicerone.Router

class RootRouter : Router() {

    fun openMainScreenAsRoot() {
        newRootScreen(NewsListScreen())
    }

    fun openNewsViewFragment(newsItem: NewsItem.Data) {
        navigateTo(NewsViewScreen(newsItem))
    }

    fun back() {
        exit()
    }

}

