package ru.khiraevmalik.theguardiannews.presentation.main

import ru.khiraevmalik.theguardiannews.mvi_base.CoroutinesMviViewModel
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.Action
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.MainNews
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.MainStore
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.State

class MainNewsViewModel(
        store: MainStore
) : CoroutinesMviViewModel<Action, Action.User, Action.Effect, State, MainNews>(store) {

    init {
        proceed(Action.User.FetchNews)
    }

}
