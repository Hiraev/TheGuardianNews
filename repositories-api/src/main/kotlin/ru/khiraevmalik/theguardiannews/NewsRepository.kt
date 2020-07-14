package ru.khiraevmalik.theguardiannews

import ru.khiraevmalik.theguardiannews.news.News

interface NewsRepository {

    suspend fun loadNews(pageNumber: Int, pageSize: Int): ContentResult<List<News>>

    suspend fun searchNews(pageNumber: Int, pageSize: Int, query: String): ContentResult<List<News>>

}
