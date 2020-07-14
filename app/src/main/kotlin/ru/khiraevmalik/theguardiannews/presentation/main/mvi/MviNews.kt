package ru.khiraevmalik.theguardiannews.presentation.main.mvi

sealed class MviNews {
    sealed class Search : MviNews() {
        object ClearSearchEditText : Search()
    }
}
