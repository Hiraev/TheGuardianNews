package ru.khiraevmalik.theguardiannews.main

import ru.khiraevmalik.theguardiannews.ContentResult
import ru.khiraevmalik.theguardiannews.news.News

interface NewsInteractor {

    suspend fun loadNews(): ContentResult<List<News>>

    suspend fun search(query: String): ContentResult<List<News>>

}
