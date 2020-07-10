package ru.khiraevmalik.theguardiannews.routing

import androidx.fragment.app.Fragment
import ru.khiraevmalik.theguardiannews.presentation.main.NewsListFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class NewsListScreen : SupportAppScreen() {

    override fun getFragment(): Fragment = NewsListFragment.newInstance()

}
