package ru.khiraevmalik.theguardiannews.presentation.main

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_main.text_view
import ru.khiraevmalik.theguardiannews.R
import ru.khiraevmalik.theguardiannews.extensions.args
import ru.khiraevmalik.theguardiannews.extensions.withParcelable
import ru.khiraevmalik.theguardiannews.presentation.BaseFragment

class NewsListFragment : BaseFragment(R.layout.fragment_main) {

    companion object {
        private const val STATE_KEY = "NewsListFragment"
        fun newInstance() = NewsListFragment().withParcelable(STATE_KEY, State("Hello"))
    }

    private val state: State by args(STATE_KEY)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_view.text = state.x
        Log.i(STATE_KEY, "onViewCreated ${this}")
    }

    @Parcelize
    data class State(val x: String) : Parcelable

}
