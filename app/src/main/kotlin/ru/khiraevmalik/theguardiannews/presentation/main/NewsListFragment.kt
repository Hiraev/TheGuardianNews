package ru.khiraevmalik.theguardiannews.presentation.main

import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.transition.Visibility
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_main.fragment_main_error_with_retry_stub
import kotlinx.android.synthetic.main.fragment_main.fragment_main_not_found_stub
import kotlinx.android.synthetic.main.fragment_main.fragment_main_progress_bar
import kotlinx.android.synthetic.main.fragment_main.fragment_main_recyclerview
import kotlinx.android.synthetic.main.fragment_main.fragment_main_search_hint_stub
import kotlinx.android.synthetic.main.fragment_main.fragment_main_status_bar
import kotlinx.android.synthetic.main.include_error_with_retry_stub.include_error_with_retry_stub_button
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_back_title_button
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_clear_edit_text_button
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_container
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_edit_text
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_search_button
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_search_group
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_title_group
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khiraevmalik.theguardiannews.R
import ru.khiraevmalik.theguardiannews.presentation.BaseFragment
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.Action
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.State
import ru.khiraevmalik.theguardiannews.utils.addOnTextChangedListener
import ru.khiraevmalik.theguardiannews.utils.addSystemTopPadding
import ru.khiraevmalik.theguardiannews.utils.hideSoftKeyboard
import ru.khiraevmalik.theguardiannews.utils.rippleClick
import ru.khiraevmalik.theguardiannews.utils.showSoftKeyboard
import ru.khiraevmalik.theguardiannews.utils.visible

class NewsListFragment : BaseFragment(R.layout.fragment_main) {

    companion object {
        private const val TOOLBAR_SEARCH_TRANSITION_DELAY = 200L
        fun newInstance() = NewsListFragment()
    }

    private val fade: Transition = Fade().apply {
        duration = TOOLBAR_SEARCH_TRANSITION_DELAY
        mode = Visibility.MODE_IN
    }

    private val showKeyboardPostDelayed = Runnable {
        showSoftKeyboard(include_search_toolbar_edit_text)
    }

    private val adapter = NewsAdapter()

    private val vm by viewModel<MainNewsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setWindowInsetsListener(view)
        initViews()
        initStateObserver()
        vm.act(Action.User.FetchNews)
    }

    override fun onStop() {
        super.onStop()
        include_search_toolbar_edit_text.removeCallbacks(showKeyboardPostDelayed)
    }

    private fun initViews() {
        include_search_toolbar_search_button.rippleClick {
            vm.act(Action.User.SearchOpen)
        }
        include_search_toolbar_back_title_button.rippleClick {
            vm.act(Action.User.SearchClose)
        }
        include_search_toolbar_clear_edit_text_button.rippleClick {
            vm.act(Action.User.SearchClear)
        }
        include_search_toolbar_edit_text.setOnFocusChangeListener { editTextView, hasFocus ->
            if (!hasFocus) hideSoftKeyboard(editTextView)
        }
        include_error_with_retry_stub_button.rippleClick {
            vm.act(Action.User.FetchNews)
        }
        include_search_toolbar_edit_text.addOnTextChangedListener { value ->
            vm.act(Action.User.SearchQuery(value.trim()))
        }
        fragment_main_recyclerview.setHasFixedSize(true)
        fragment_main_recyclerview.adapter = adapter
    }

    private fun initStateObserver() {
        vm.stateLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Search.Idle -> showSearchIdle()
                is State.Search.EmptyQuery -> showSearchEmptyQuery()
                is State.Search.Loading -> showSearchLoading()
                is State.Search.Error -> showSearchError()
                is State.Search.NotFound -> showSearchNotFound()
                is State.Search.Success -> showSearchResults(state.news)

                is State.Fetch.Loading -> showStateLoading()
                is State.Fetch.Error -> showStateErrorLoading()
                is State.Fetch.Success -> showStateLoadedNews(state.news)
            }
            updateState(state)
        })
    }

    private fun showSearchNotFound() {
        showOrHideSearchToolbar(true)
    }

    private fun showStateLoading() {
        showOrHideSearchToolbar(false)
    }

    private fun showSearchLoading() {
        showOrHideSearchToolbar(true)
    }

    private fun showSearchError() {
        showOrHideSearchToolbar(true)
    }

    private fun showSearchResults(result: List<NewsItem>) {
        showOrHideSearchToolbar(true)
        adapter.submitList(result)
    }

    private fun showStateErrorLoading() {
        showOrHideSearchToolbar(false)
    }

    private fun showSearchEmptyQuery() {
        showOrHideSearchToolbar(true)
        include_search_toolbar_edit_text.text?.clear()
    }

    private fun showSearchIdle() {
        showOrHideSearchToolbar(true)
    }

    private fun showStateLoadedNews(news: List<NewsItem>) {
        adapter.submitList(news)
        showOrHideSearchToolbar(false)
    }

    private fun setWindowInsetsListener(view: View) {
        view.addSystemTopPadding(fragment_main_status_bar)
    }

    private fun showOrHideSearchToolbar(show: Boolean) {
        TransitionManager.beginDelayedTransition(include_search_toolbar_container, fade)
        include_search_toolbar_search_group.visible(show)
        include_search_toolbar_title_group.visible(!show)
        if (show) focusSearchEditText() else hideSoftKeyboard(include_search_toolbar_edit_text)
    }

    private fun focusSearchEditText() {
        include_search_toolbar_edit_text.postDelayed(showKeyboardPostDelayed, TOOLBAR_SEARCH_TRANSITION_DELAY)
    }

    private fun updateState(state: State) {
        fragment_main_progress_bar.visible(state is State.Fetch.Loading || state is State.Search.Loading)
        fragment_main_error_with_retry_stub.visible(state is State.Search.NotFound)
        fragment_main_not_found_stub.visible(state is State.Search.NotFound)
        fragment_main_error_with_retry_stub.visible(state is State.Fetch.Error)
        fragment_main_recyclerview.visible(state is State.Fetch.Success || state is State.Search.Success)
        fragment_main_search_hint_stub.visible(state is State.Search.NotFound || state is State.Search.Idle)
    }

}
