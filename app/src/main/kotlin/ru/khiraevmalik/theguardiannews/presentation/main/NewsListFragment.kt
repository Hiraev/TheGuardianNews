package ru.khiraevmalik.theguardiannews.presentation.main

import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.transition.Visibility
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_main.fragment_main_container
import kotlinx.android.synthetic.main.fragment_main.fragment_main_error_with_retry_stub
import kotlinx.android.synthetic.main.fragment_main.fragment_main_no_data_with_retry_stub
import kotlinx.android.synthetic.main.fragment_main.fragment_main_not_found_stub
import kotlinx.android.synthetic.main.fragment_main.fragment_main_progress_bar
import kotlinx.android.synthetic.main.fragment_main.fragment_main_recyclerview
import kotlinx.android.synthetic.main.fragment_main.fragment_main_scroll_up_button
import kotlinx.android.synthetic.main.fragment_main.fragment_main_search_hint_stub
import kotlinx.android.synthetic.main.fragment_main.fragment_main_search_recyclerview
import kotlinx.android.synthetic.main.fragment_main.fragment_main_status_bar
import kotlinx.android.synthetic.main.include_error_with_retry_stub.include_error_with_retry_stub_button
import kotlinx.android.synthetic.main.include_no_data_with_retry_stub.include_no_data_with_retry_stub_button
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_back_title_button
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_clear_edit_text_button
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_container
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_edit_text
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_search_button
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_search_group
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_title_group
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khiraevmalik.theguardiannews.R
import ru.khiraevmalik.theguardiannews.base.PagingStatus
import ru.khiraevmalik.theguardiannews.presentation.BaseFragment
import ru.khiraevmalik.theguardiannews.presentation.main.adapter.NewsAdapter
import ru.khiraevmalik.theguardiannews.presentation.main.adapter.NewsItem
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.Action
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.MainNews
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.State
import ru.khiraevmalik.theguardiannews.utils.addOnTextChangedListener
import ru.khiraevmalik.theguardiannews.utils.addSystemTopPadding
import ru.khiraevmalik.theguardiannews.utils.hideSoftKeyboard
import ru.khiraevmalik.theguardiannews.utils.isVisible
import ru.khiraevmalik.theguardiannews.utils.rippleClick
import ru.khiraevmalik.theguardiannews.utils.showSoftKeyboard
import ru.khiraevmalik.theguardiannews.utils.visible
import ru.khiraevmalik.theguardiannews.utils.visibleWithCheck

class NewsListFragment : BaseFragment(R.layout.fragment_main) {

    companion object {
        private const val TRANSITION_DURATION = 200L
        private const val MIN_FIRST_VISIBLE_POSITION_TO_SHOW_SCROLL_UP_BUTTON = 15
        private const val PREFETCH_DISTANCE = 5
        fun newInstance() = NewsListFragment()
    }

    private val fade: Transition = Fade().apply {
        duration = TRANSITION_DURATION
        mode = Visibility.MODE_IN
    }

    private val scrollUpButtonFade: Transition = Fade().apply { duration = TRANSITION_DURATION }

    private val showKeyboardPostDelayed = Runnable {
        showSoftKeyboard(include_search_toolbar_edit_text)
    }

    private val adapter = NewsAdapter { vm.proceed(Action.User.Retry) }
    private val searchAdapter = NewsAdapter()
    private val scrollListener = LinearLayoutManagerRecyclerViewOnScrollListener(PREFETCH_DISTANCE) {
        vm.proceed(Action.User.FetchMore)
    }
    private val scrollListenerForScrollUpButton = ScrollUpButtonVisibilityOnScrollListener(MIN_FIRST_VISIBLE_POSITION_TO_SHOW_SCROLL_UP_BUTTON) { visible ->
        onScrollUpVisibilityChanged(visible)
    }

    private val vm by viewModel<MainNewsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setWindowInsetsListener(view)
        initViews()
        initStateObserver()
    }

    override fun onStop() {
        super.onStop()
        include_search_toolbar_edit_text.removeCallbacks(showKeyboardPostDelayed)
    }

    private fun initViews() {
        include_search_toolbar_search_button.rippleClick {
            vm.proceed(Action.User.SearchOpen)
            onScrollUpVisibilityChanged(false)
        }
        include_search_toolbar_back_title_button.rippleClick {
            vm.proceed(Action.User.SearchClose)
            onScrollUpVisibilityChanged(false)
        }
        include_search_toolbar_clear_edit_text_button.rippleClick {
            vm.proceed(Action.User.SearchClear)
        }
        include_search_toolbar_edit_text.setOnFocusChangeListener { editTextView, hasFocus ->
            if (!hasFocus) hideSoftKeyboard(editTextView)
        }
        include_error_with_retry_stub_button.rippleClick {
            vm.proceed(Action.User.Retry)
        }
        include_search_toolbar_edit_text.addOnTextChangedListener { value ->
            vm.proceed(Action.User.SearchQuery(value.trim()))
        }
        include_no_data_with_retry_stub_button.rippleClick {
            vm.proceed(Action.User.FetchNews)
        }
        fragment_main_scroll_up_button.rippleClick {
            if (fragment_main_recyclerview.isVisible()) {
                fragment_main_recyclerview.scrollToPosition(0)
            } else if (fragment_main_search_recyclerview.isVisible()) {
                fragment_main_search_recyclerview.scrollToPosition(0)
            }
            onScrollUpVisibilityChanged(false)
        }
        fragment_main_recyclerview.setHasFixedSize(true)
        fragment_main_recyclerview.adapter = adapter
        fragment_main_recyclerview.addOnScrollListener(scrollListener)
        fragment_main_recyclerview.addOnScrollListener(scrollListenerForScrollUpButton)
        fragment_main_search_recyclerview.addOnScrollListener(scrollListenerForScrollUpButton)
        fragment_main_search_recyclerview.setHasFixedSize(true)
        fragment_main_search_recyclerview.adapter = searchAdapter

        scrollUpButtonFade.addTarget(fragment_main_scroll_up_button)
    }

    private fun initStateObserver() {
        vm.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Search.Loading,
                is State.Search.Error,
                is State.Search.NotFound,
                is State.Search.Idle -> {
                    showOrHideSearchToolbar(true)
                }
                is State.Search.Success -> {
                    showOrHideSearchToolbar(true)
                    searchAdapter.submitList(state.news)
                }
                is State.Fetch.EmptyData,
                is State.Fetch.Loading,
                is State.Fetch.Error -> {
                    showOrHideSearchToolbar(false)
                }
                is State.Fetch.Success -> {
                    showOrHideSearchToolbar(false)
                    if (state.pagingStatus == PagingStatus.HAS_MORE) scrollListener.enable() else scrollListener.disable()

                    adapter.submitList(state.news + when (state.pagingStatus) {
                        PagingStatus.FULL -> listOf(NewsItem.FullDataItem)
                        PagingStatus.ERROR -> listOf(NewsItem.ErrorLoadingMore)
                        PagingStatus.LOADING -> listOf(NewsItem.LoadingMore)
                        else -> emptyList()
                    })
                }
            }
            updateViewsVisibility(state)
        })
        vm.events.observe(viewLifecycleOwner, Observer { event ->
            when (event) {
                is MainNews.Search.ClearSearchEditText -> include_search_toolbar_edit_text.text?.clear()
            }
        })
    }

    private fun setWindowInsetsListener(view: View) {
        view.addSystemTopPadding(fragment_main_status_bar)
    }

    private fun showOrHideSearchToolbar(show: Boolean) {
        TransitionManager.beginDelayedTransition(include_search_toolbar_container, fade)
        include_search_toolbar_search_group.visibleWithCheck(show)
        include_search_toolbar_title_group.visibleWithCheck(!show)
        if (show) focusSearchEditText() else hideSoftKeyboard(include_search_toolbar_edit_text)
    }

    private fun focusSearchEditText() {
        include_search_toolbar_edit_text.postDelayed(showKeyboardPostDelayed, TRANSITION_DURATION)
    }

    private fun onScrollUpVisibilityChanged(visible: Boolean) {
        if (visible xor fragment_main_scroll_up_button.isVisible()) {
            TransitionManager.beginDelayedTransition(fragment_main_container, scrollUpButtonFade)
            fragment_main_scroll_up_button.visibleWithCheck(visible)
        }
    }

    private fun updateViewsVisibility(state: State) {
        fragment_main_progress_bar.visibleWithCheck(state is State.Fetch.Loading || state is State.Search.Loading)
        fragment_main_not_found_stub.visibleWithCheck(state is State.Search.NotFound)
        fragment_main_no_data_with_retry_stub.visibleWithCheck(state is State.Fetch.EmptyData)
        fragment_main_error_with_retry_stub.visibleWithCheck(state is State.Fetch.Error || state is State.Search.Error)
        fragment_main_search_recyclerview.visibleWithCheck(state is State.Search.Success)
        fragment_main_recyclerview.visibleWithCheck(state is State.Fetch.Success)
        fragment_main_search_hint_stub.visibleWithCheck(state is State.Search.Idle)
    }

}
