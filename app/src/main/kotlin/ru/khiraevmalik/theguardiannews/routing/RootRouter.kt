package ru.khiraevmalik.theguardiannews.routing

import ru.terrakok.cicerone.Router

class RootRouter : Router() {

    fun openMainScreenAsRoot() {
        newRootScreen(NewsListScreen())
    }

}

