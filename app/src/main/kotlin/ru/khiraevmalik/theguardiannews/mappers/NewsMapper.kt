package ru.khiraevmalik.theguardiannews.mappers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.khiraevmalik.theguardiannews.news.News
import ru.khiraevmalik.theguardiannews.presentation.main.adapter.NewsItem

object NewsMapper {

    suspend fun mapToNewsItems(news: List<News>): List<NewsItem> = withContext(Dispatchers.Default) {
        news.map(::mapToNewsItem)
    }

    private fun mapToNewsItem(news: News): NewsItem = NewsItem.Data(
            id = news.id,
            title = news.title,
            subtitle = news.subtitle,
            imageUrl = news.imageUrl,
            body = news.text
    )
}
