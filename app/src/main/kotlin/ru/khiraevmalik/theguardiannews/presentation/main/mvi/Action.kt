package ru.khiraevmalik.theguardiannews.presentation.main.mvi

import ru.khiraevmalik.theguardiannews.presentation.main.NewsItem

sealed class Action {

    // Actions from user
    sealed class User : Action() {
        object SearchOpen : User()
        object SearchClose : User()
        object SearchClear : User()
        class SearchQuery(val query: String) : User()

        object FetchNews : User()
    }

    // Effects
    sealed class Effect : Action() {
        object SearchLoading : Effect()
        object SearchError : Effect()
        object SearchEmpty : Effect()
        class SearchSuccess(val news: List<NewsItem>) : Effect()

        object FetchLoading : Effect()
        object FetchError : Effect()
        class FetchSuccess(val news: List<NewsItem>) : Effect()
    }

}
