package ru.khiraevmalik.theguardiannews.presentation.main.adapter

sealed class NewsItem {
    data class Data(
            val id: String,
            val title: String,
            val subtitle: String,
            val imageUrl: String
    ) : NewsItem()

    object LoadingMore : NewsItem()
    object ErrorLoadingMore : NewsItem()
    object FullDataItem : NewsItem()
}
