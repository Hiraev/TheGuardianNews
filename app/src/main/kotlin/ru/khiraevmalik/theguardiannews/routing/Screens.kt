package ru.khiraevmalik.theguardiannews.routing

import androidx.fragment.app.Fragment
import ru.khiraevmalik.theguardiannews.presentation.main.NewsListFragment
import ru.khiraevmalik.theguardiannews.presentation.main.adapter.NewsItem
import ru.khiraevmalik.theguardiannews.presentation.news_view.NewsViewFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class NewsListScreen : SupportAppScreen() {

    override fun getFragment(): Fragment = NewsListFragment.newInstance()

}

class NewsViewScreen(private val newsItem: NewsItem.Data) : SupportAppScreen() {

    override fun getFragment(): Fragment = NewsViewFragment.newInstance(newsItem)

}
