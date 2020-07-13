package ru.khiraevmalik.theguardiannews

sealed class Result<out T>(open val data: T?) {

    class Success<out T>(override val data: T) : Result<T>(data)

    class Error<out T>(val throwable: Throwable?, override val data: T? = null) : Result<T>(data)

}
