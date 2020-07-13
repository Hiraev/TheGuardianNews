package ru.khiraevmalik.theguardiannews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.khiraevmalik.theguardiannews.mvi_no_reactive.Store

abstract class CoroutinesMviViewModel<A, U, E, S>(
        private val store: Store<A, U, E, S>
) : CoroutinesViewModel() where U : A, E : A {

    private val mState = MutableLiveData<S>()

    val state: LiveData<S> = mState

    init {
        store.setOnStateChangeListener { newState ->
            mState.postValue(newState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }

    fun act(action: A) {
        store.act(action)
    }

}
