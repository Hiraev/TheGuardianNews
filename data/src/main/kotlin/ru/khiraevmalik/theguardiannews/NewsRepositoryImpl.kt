package ru.khiraevmalik.theguardiannews

import ru.khiraevmalik.theguardiannews.api.Api
import ru.khiraevmalik.theguardiannews.api.NewsApi
import ru.khiraevmalik.theguardiannews.converters.NewsConverter
import ru.khiraevmalik.theguardiannews.news.News

class NewsRepositoryImpl(
        private val newsApi: NewsApi
) : NewsRepository {

    override suspend fun loadNews(pageNumber: Int, pageSize: Int): ContentResult<List<News>> {
        return try {
            val result = newsApi.fetchNews(pageNumber, pageSize, Api.TheGuardian.Params.OrderByValue.newest)
            ContentResult.Success(NewsConverter.convert(result))
        } catch (throwable: Throwable) {
            ContentResult.Error(throwable)
        }
    }

    override suspend fun searchNews(pageNumber: Int, pageSize: Int, query: String): ContentResult<List<News>> {
        return try {
            val result = newsApi.searchNews(pageNumber, pageSize, query, Api.TheGuardian.Params.OrderByValue.newest)
            ContentResult.Success(NewsConverter.convert(result))
        } catch (throwable: Throwable) {
            ContentResult.Error(throwable)
        }
    }

}
