package ru.khiraevmalik.theguardiannews.presentation.main.mvi

import android.util.Log
import ru.khiraevmalik.theguardiannews.mvi_no_reactive.BuildConfig
import ru.khiraevmalik.theguardiannews.mvi_no_reactive.Middleware
import ru.khiraevmalik.theguardiannews.mvi_no_reactive.Reducer
import ru.khiraevmalik.theguardiannews.mvi_no_reactive.Store

class MainStore(
        reducer: Reducer<State, Action>,
        middleware: List<Middleware<Action, Action.Effect, MainNews>>
) : Store<Action, Action.User, Action.Effect, State, MainNews>(
        reducer, middleware, initialState = State.Fetch.Loading
) {

    companion object {
        private const val DEBUG_TAG = "MVI"
        private const val DEBUG_STRING_PATTERN = "%s -> %s by %s"
    }

    override fun onAct(action: Action, oldState: State, newState: State) {
        if (BuildConfig.DEBUG) {
            Log.d(
                    DEBUG_TAG,
                    String.format(
                            DEBUG_STRING_PATTERN,
                            oldState::class.qualifiedName?.withoutPackageName() ?: "",
                            newState::class.qualifiedName?.withoutPackageName() ?: "",
                            action::class.qualifiedName?.withoutPackageName() ?: ""
                    )
            )
        }
    }

}

private val regex = Regex("[A-Z].*")
private fun String.withoutPackageName() = split('.').filter { it.matches(regex) }.joinToString(".")
