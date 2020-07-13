package ru.khiraevmalik.theguardiannews.mvi_no_reactive

/**
 * Produces new state (S) from action (A) and current state (A)
 *
 * To understand S, A @see [ru.khiraevmalik.theguardiannews.mvi_no_reactive.Store]
 */
interface Reducer<S, A> {

    fun reduce(action: A, state: S): S

}
