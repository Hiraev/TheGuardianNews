package ru.khiraevmalik.theguardiannews.main

import ru.khiraevmalik.theguardiannews.Result
import ru.khiraevmalik.theguardiannews.news.News

interface NewsInteractor {

    suspend fun loadNews(): Result<List<News>>

    suspend fun search(query: String): Result<List<News>>

}
