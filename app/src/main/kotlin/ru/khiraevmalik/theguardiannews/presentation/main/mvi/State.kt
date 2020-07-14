package ru.khiraevmalik.theguardiannews.presentation.main.mvi

import ru.khiraevmalik.theguardiannews.presentation.main.NewsItem

sealed class State {

    sealed class Fetch : State() {
        object Loading : Fetch()
        object Error : Fetch()
        object EmptyData : Fetch()
        class Success(val news: List<NewsItem>) : Fetch()
        class FullData(val news: List<NewsItem>) : Fetch()
    }

    sealed class Search : State() {
        object Idle : Search()
        object NotFound : Search()
        object Loading : Search()
        class Error(val lastQuery: String) : Search()
        class Success(val news: List<NewsItem>) : Search()
    }

}
