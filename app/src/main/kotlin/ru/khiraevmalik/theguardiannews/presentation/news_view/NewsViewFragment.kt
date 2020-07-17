package ru.khiraevmalik.theguardiannews.presentation.news_view

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_news.fragment_news_back_button
import kotlinx.android.synthetic.main.fragment_news.fragment_news_body
import kotlinx.android.synthetic.main.fragment_news.fragment_news_image
import kotlinx.android.synthetic.main.fragment_news.fragment_news_status_bar
import kotlinx.android.synthetic.main.fragment_news.fragment_news_subtitle
import kotlinx.android.synthetic.main.fragment_news.fragment_news_title
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khiraevmalik.theguardiannews.R
import ru.khiraevmalik.theguardiannews.presentation.BaseFragment
import ru.khiraevmalik.theguardiannews.presentation.main.adapter.NewsItem
import ru.khiraevmalik.theguardiannews.utils.addSystemTopPadding
import ru.khiraevmalik.theguardiannews.utils.args
import ru.khiraevmalik.theguardiannews.utils.rippleClick
import ru.khiraevmalik.theguardiannews.utils.withParcelable

class NewsViewFragment : BaseFragment(R.layout.fragment_news) {

    companion object {
        private const val TAG = "NewsViewFragment"
        fun newInstance(newsItem: NewsItem.Data) = NewsViewFragment().withParcelable(TAG, newsItem)
    }

    private val state by args<NewsItem.Data>(TAG)
    private val vm by viewModel<NewsViewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.addSystemTopPadding(fragment_news_status_bar)
        Glide.with(view)
                .load(state.imageUrl)
                .centerCrop()
                .into(fragment_news_image)
        fragment_news_title.text = state.title
        fragment_news_subtitle.text = state.subtitle
        fragment_news_body.text = state.body
        fragment_news_back_button.rippleClick {
            vm.back()
        }
    }


}
