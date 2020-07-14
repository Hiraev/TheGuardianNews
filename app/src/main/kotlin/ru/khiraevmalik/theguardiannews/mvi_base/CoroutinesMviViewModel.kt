package ru.khiraevmalik.theguardiannews.mvi_base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.khiraevmalik.theguardiannews.CoroutinesViewModel
import ru.khiraevmalik.theguardiannews.mvi_no_reactive.Store
import ru.khiraevmalik.theguardiannews.mvi_no_reactive.lifecycle.SingleLiveData

abstract class CoroutinesMviViewModel<A, U, E, S, Ev>(
        private val store: Store<A, U, E, S, Ev>
) : CoroutinesViewModel() where U : A, E : A {

    private val mState = MutableLiveData<S>()
    private val mEvents = SingleLiveData<Ev>()

    val state: LiveData<S> = mState
    val events: LiveData<Ev> = mEvents

    init {
        store.setOnStateChangeListener { newState ->
            mState.postValue(newState)
        }
        store.setEventsListener { event ->
            mEvents.postValue(event)
        }
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }

    fun proceed(action: A) {
        store.proceed(action)
    }

}
