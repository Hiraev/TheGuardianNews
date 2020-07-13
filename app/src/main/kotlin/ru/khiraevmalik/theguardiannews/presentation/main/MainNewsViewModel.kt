package ru.khiraevmalik.theguardiannews.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.khiraevmalik.theguardiannews.CoroutinesMviViewModel
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.Action
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.MainStore
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.State

class MainNewsViewModel(
        store: MainStore
) : CoroutinesMviViewModel<Action, Action.User, Action.Effect, State>(store) {

    private val mState = MutableLiveData<State>()

    val state: LiveData<State> = mState

    init {
        store.setOnStateUpdateListener { newState ->
            mState.postValue(newState)
        }
        act(Action.User.FetchNews)
    }

}
