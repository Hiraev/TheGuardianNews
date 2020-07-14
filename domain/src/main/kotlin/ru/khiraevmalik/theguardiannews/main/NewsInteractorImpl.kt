package ru.khiraevmalik.theguardiannews.main

import ru.khiraevmalik.theguardiannews.BaseInteractor
import ru.khiraevmalik.theguardiannews.ContentResult
import ru.khiraevmalik.theguardiannews.NewsRepository
import ru.khiraevmalik.theguardiannews.news.News

class NewsInteractorImpl(
        private val newsRepository: NewsRepository
) : BaseInteractor(), NewsInteractor {

    companion object {
        private const val SEARCH_PAGE_SIZE = 30
        private const val SEARCH_PAGE_NUMBER = 1
    }

    override suspend fun loadNews(pageNumber: Int, pageSize: Int): ContentResult<List<News>> = withDefault {
        newsRepository.loadNews(pageNumber, pageSize)
    }

    override suspend fun search(query: String): ContentResult<List<News>> = withDefault {
        newsRepository.searchNews(SEARCH_PAGE_NUMBER, SEARCH_PAGE_SIZE, query)
    }

}
