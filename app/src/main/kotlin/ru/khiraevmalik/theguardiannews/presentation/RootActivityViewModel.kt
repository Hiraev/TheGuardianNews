package ru.khiraevmalik.theguardiannews.presentation

import ru.khiraevmalik.theguardiannews.CoroutinesViewModel
import ru.khiraevmalik.theguardiannews.routing.RootNavigator
import ru.khiraevmalik.theguardiannews.routing.RootRouter
import ru.terrakok.cicerone.NavigatorHolder

class RootActivityViewModel(
        private val navigatorHolder: NavigatorHolder,
        private val router: RootRouter
) : CoroutinesViewModel() {

    fun openNewsListAsRoot() {
        router.openMainScreenAsRoot()
    }

    fun setNavigator(navigator: RootNavigator) {
        navigatorHolder.setNavigator(navigator)
    }

    fun removeNavigator() {
        navigatorHolder.removeNavigator()
    }

}
