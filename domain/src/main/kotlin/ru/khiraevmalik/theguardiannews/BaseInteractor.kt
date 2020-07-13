package ru.khiraevmalik.theguardiannews

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

abstract class BaseInteractor {

    protected suspend fun <T> withDefault(
            job: Job? = null,
            block: suspend CoroutineScope.() -> T
    ) = withContext(job?.let { Dispatchers.Default + job } ?: Dispatchers.Default, block)

}
