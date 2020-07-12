package ru.khiraevmalik.theguardiannews.mappers

import ru.khiraevmalik.theguardiannews.Result

object ResultMapper {

    suspend fun <T, R> map(result: Result<T>, mapper: suspend (T) -> (R), lastValue: R?): Result<R> = when (result) {
        is Result.Success -> Result.Success(mapper.invoke(result.data))
        is Result.Error -> Result.Error(result.throwable, lastValue)
        is Result.Loading -> Result.Loading(lastValue)
    }

}

suspend fun <T, R> Result<T>.map(
        mapper: suspend (T) -> (R),
        lastValue: R?
): Result<R> = ResultMapper.map(this, mapper, lastValue)
