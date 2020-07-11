package ru.khiraevmalik.theguardiannews.presentation.main

import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.transition.Visibility
import android.view.View
import kotlinx.android.synthetic.main.fragment_main.fragment_main_recyclerview
import kotlinx.android.synthetic.main.fragment_main.fragment_main_status_bar
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_back_title_button
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_clear_edit_text_button
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_container
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_edit_text
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_search_button
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_search_group
import kotlinx.android.synthetic.main.include_search_toolbar.include_search_toolbar_title_group
import ru.khiraevmalik.theguardiannews.R
import ru.khiraevmalik.theguardiannews.presentation.BaseFragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setWindowInsetsListener(view)
        include_search_toolbar_search_button.rippleClick {
            showOrHideSearchToolbar(true)
        }
        include_search_toolbar_back_title_button.rippleClick {
            showOrHideSearchToolbar(false)
        }
        include_search_toolbar_clear_edit_text_button.rippleClick {
            include_search_toolbar_edit_text.text?.clear()
        }
        include_search_toolbar_edit_text.setOnFocusChangeListener { editTextView, hasFocus ->
            if (!hasFocus) hideSoftKeyboard(editTextView)
        }
        fragment_main_recyclerview.setHasFixedSize(true)
        fragment_main_recyclerview.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        include_search_toolbar_edit_text.removeCallbacks(showKeyboardPostDelayed)
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

}
