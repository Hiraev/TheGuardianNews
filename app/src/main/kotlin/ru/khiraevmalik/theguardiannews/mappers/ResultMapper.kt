package ru.khiraevmalik.theguardiannews.mappers

import ru.khiraevmalik.theguardiannews.ContentResult

object ResultMapper {

    suspend fun <T, R> map(result: ContentResult<T>, lastValue: R? = null, mapper: suspend (T) -> (R)): ContentResult<R> = when (result) {
        is ContentResult.Success -> ContentResult.Success(mapper.invoke(result.data))
        is ContentResult.Error -> ContentResult.Error(result.throwable, lastValue)
    }

}

suspend fun <T, R> ContentResult<T>.map(
        lastValue: R? = null,
        mapper: suspend (T) -> (R)
): ContentResult<R> = ResultMapper.map(this, lastValue, mapper)
