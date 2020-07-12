package ru.khiraevmalik.theguardiannews

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseInteractor {

    protected suspend fun <T> withDefault(
            block: suspend CoroutineScope.() -> T
    ) = withContext(Dispatchers.Default, block)

}
