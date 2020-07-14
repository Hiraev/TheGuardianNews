package ru.khiraevmalik.theguardiannews

import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTestRule
import ru.khiraevmalik.theguardiannews.main.NewsInteractor

class NewsInteractorTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(interactors, dataProvider)
    }

    val newsInteractor by lazy {
        koinTestRule.koin.get<NewsInteractor>()
    }

    @Test
    fun testRequest() {
        runBlocking {
            val result = newsInteractor.search("Hmdksmkldmflvnjdfknvkmflmello")
            assert(result is ContentResult.Success)
        }
    }

}
