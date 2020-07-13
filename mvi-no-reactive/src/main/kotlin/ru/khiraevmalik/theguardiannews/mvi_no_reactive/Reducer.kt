package ru.khiraevmalik.theguardiannews.mvi_no_reactive

interface Reducer<S, A> {

    fun reduce(action: A, state: S): S

}
