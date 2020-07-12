package ru.khiraevmalik.theguardiannews.main

import kotlinx.coroutines.delay
import ru.khiraevmalik.theguardiannews.BaseInteractor
import ru.khiraevmalik.theguardiannews.Result
import ru.khiraevmalik.theguardiannews.news.News
import kotlin.random.Random

class NewsInteractorImpl : BaseInteractor(), NewsInteractor {

    // TODO
    override suspend fun loadNews(): Result<List<News>> = withDefault {
        delay(2000)
        Result.Success(List(15) {
            News(Random.nextInt().toString(), "Hekki", "hbjfgv", "jjnkdnu unjnrkfnkjnjk ", "kjnjkgn")
        })
        Result.Error<List<News>>(null)
    }

    // TODO
    override suspend fun search(query: String): Result<List<News>> = withDefault {
        delay(2000)
        Result.Success(List(2) {
            News(Random.nextInt().toString(), "Search", "Result", "Something", "km")
        })
    }

}
