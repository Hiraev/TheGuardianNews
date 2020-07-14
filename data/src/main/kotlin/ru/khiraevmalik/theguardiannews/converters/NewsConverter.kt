package ru.khiraevmalik.theguardiannews.converters

import ru.khiraevmalik.theguardiannews.model.ResponseWrapper
import ru.khiraevmalik.theguardiannews.news.News

object NewsConverter {

    fun convert(news: ResponseWrapper): List<News> {
        val results = news.response.results
        return results.map { body ->
            News(
                    id = body.id,
                    title = body.fields.headline,
                    subtitle = body.fields.trailText,
                    text = body.fields.bodyText,
                    imageUrl = body.fields.thumbnail
            )
        }
    }

}
