package ru.khiraevmalik.theguardiannews

sealed class ContentResult<out T>(open val data: T?) {

    class Success<out T>(override val data: T) : ContentResult<T>(data)

    class Error<out T>(val throwable: Throwable?, override val data: T? = null) : ContentResult<T>(data)

}
