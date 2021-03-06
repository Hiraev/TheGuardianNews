package ru.khiraevmalik.theguardiannews.presentation.main.mvi

import ru.khiraevmalik.theguardiannews.base.PagingStatus
import ru.khiraevmalik.theguardiannews.presentation.main.adapter.NewsItem

sealed class Action {

    // Actions from user
    sealed class User : Action() {
        object SearchOpen : User()
        object SearchClose : User()
        object SearchClear : User()
        class SearchQuery(val query: String) : User()

        object FetchNews : User()
        object FetchMore : User()

        object Retry : User()
    }

    // Effects
    sealed class Effect : Action() {
        object SearchLoading : Effect()
        class SearchError(val lastQuery: String) : Effect()
        object SearchEmpty : Effect()
        object SearchClose : Effect()
        class SearchSuccess(val news: List<NewsItem>) : Effect()

        object FetchLoading : Effect()
        object FetchError : Effect()
        class FetchSuccess(val news: List<NewsItem>, val pagingStatus: PagingStatus) : Effect()
    }

}
