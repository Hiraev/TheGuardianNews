package ru.khiraevmalik.theguardiannews.mappers

import ru.khiraevmalik.theguardiannews.Result

object ResultMapper {

    suspend fun <T, R> map(result: Result<T>, lastValue: R? = null, mapper: suspend (T) -> (R)): Result<R> = when (result) {
        is Result.Success -> Result.Success(mapper.invoke(result.data))
        is Result.Error -> Result.Error(result.throwable, lastValue)
    }

}

suspend fun <T, R> Result<T>.map(
        lastValue: R? = null,
        mapper: suspend (T) -> (R)
): Result<R> = ResultMapper.map(this, lastValue, mapper)
