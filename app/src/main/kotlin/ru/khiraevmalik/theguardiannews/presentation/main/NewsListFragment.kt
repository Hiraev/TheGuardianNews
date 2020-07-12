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
import ru.khiraevmalik.theguardiannews.Result
import ru.khiraevmalik.theguardiannews.presentation.BaseFragment
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

    private var currentState = State.LOADING

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setWindowInsetsListener(view)
        initViews()
        initObservers()
        vm.loadNews()
    }

    override fun onStop() {
        super.onStop()
        include_search_toolbar_edit_text.removeCallbacks(showKeyboardPostDelayed)
    }

    private fun initObservers() {
        vm.news.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> showStateLoading()
                is Result.Error -> showStateErrorLoading()
                is Result.Success -> showStateLoadedNews(result.data)
            }
        })
        vm.searchState.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is MainNewsViewModel.SearchState.Loading -> showSearchLoading()
                is MainNewsViewModel.SearchState.NotFound -> showSearchError()
                is MainNewsViewModel.SearchState.Error -> showSearchError()
                is MainNewsViewModel.SearchState.EmptyQuery -> showSearchEmptyQuery()
                is MainNewsViewModel.SearchState.Success -> showSearchResults(result.news)
            }
        })
    }

    private fun initViews() {
        include_search_toolbar_search_button.rippleClick {
            showOrHideSearchToolbar(true)
            showSearchEmptyQuery()
            vm.requestLastSearchResult()
        }
        include_search_toolbar_back_title_button.rippleClick {
            showOrHideSearchToolbar(false)
            vm.requestLastNewsResult()
        }
        include_search_toolbar_clear_edit_text_button.rippleClick {
            include_search_toolbar_edit_text.text?.clear()
        }
        include_search_toolbar_edit_text.setOnFocusChangeListener { editTextView, hasFocus ->
            if (!hasFocus) hideSoftKeyboard(editTextView)
        }
        include_error_with_retry_stub_button.rippleClick {
            vm.loadNews()
            showStateLoading()
        }
        include_search_toolbar_edit_text.addOnTextChangedListener { value ->
            vm.searchQuery(value.trim())
        }
        fragment_main_recyclerview.setHasFixedSize(true)
        fragment_main_recyclerview.adapter = adapter
    }

    private fun showStateLoading() {
        setState(State.LOADING)
    }

    private fun showSearchLoading() {
        setState(State.SEARCH_LOADING)
    }

    private fun showSearchError() {
        setState(State.SEARCH_NOT_FOUND)
    }

    private fun showSearchResults(result: List<NewsItem>) {
        adapter.submitList(result)
        setState(State.SEARCH_SUCCESS)
    }

    private fun showStateErrorLoading() {
        setState(State.ERROR_LOADING)
    }

    private fun showSearchEmptyQuery() {
        setState(State.SEARCH_EMPTY_QUERY)
    }

    private fun showStateLoadedNews(news: List<NewsItem>) {
        adapter.submitList(news)
        setState(State.SUCCESS)
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

    private fun setState(newState: State) {
        if (currentState == newState) return
        fragment_main_progress_bar.visible(newState == State.LOADING || newState == State.SEARCH_LOADING)
        fragment_main_error_with_retry_stub.visible(newState == State.SEARCH_NOT_FOUND)
        fragment_main_not_found_stub.visible(newState == State.SEARCH_NOT_FOUND)
        fragment_main_error_with_retry_stub.visible(newState == State.ERROR_LOADING)
        fragment_main_recyclerview.visible(newState == State.SUCCESS || newState == State.SEARCH_SUCCESS)
        fragment_main_search_hint_stub.visible(newState == State.SEARCH_EMPTY_QUERY)
        currentState = newState
    }

    private enum class State {
        LOADING, SEARCH_EMPTY_QUERY, SEARCH_NOT_FOUND, ERROR_LOADING, SUCCESS, SEARCH_LOADING, SEARCH_SUCCESS
    }

}
