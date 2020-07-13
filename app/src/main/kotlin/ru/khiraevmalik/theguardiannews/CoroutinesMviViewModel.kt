package ru.khiraevmalik.theguardiannews

import ru.khiraevmalik.theguardiannews.mvi_no_reactive.Store

abstract class CoroutinesMviViewModel<A, U, E, S>(
        val store: Store<A, U, E, S>
) : CoroutinesViewModel() where U : A, E : A {

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }

    fun act(action: A) {
        store.act(action)
    }

}
