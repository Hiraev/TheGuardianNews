package ru.khiraevmalik.theguardiannews.mvi_base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.khiraevmalik.theguardiannews.mvi_no_reactive.Middleware

abstract class DisposableMiddleware<A, E, S, N>(
        job: Job = Job(),
        dispatcher: CoroutineDispatcher = Dispatchers.Default
) : Middleware<A, E, S, N>() where E : A {

    private val scope = CoroutineScope(job + dispatcher)

    override fun onDispose() {
        scope.cancel()
    }

    fun effectOnMain(effect: E) {
        scope.launch(Dispatchers.Main) {
            effect(effect)
        }
    }

    fun newsOnMain(news: N) {
        scope.launch(Dispatchers.Main) {
            news(news)
        }
    }

    fun launch(block: suspend CoroutineScope.() -> Unit) = scope.launch(block = block)

}
