package ru.khiraevmalik.theguardiannews.presentation.main.mvi

sealed class MainNews {
    sealed class Search : MainNews() {
        object ClearSearchEditText : Search()
    }
}
