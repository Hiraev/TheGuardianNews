package ru.khiraevmalik.theguardiannews.presentation.main

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollUpButtonVisibilityOnScrollListener(
        private val minimalFirstVisiblePosition: Int,
        private val changeVisibilityCallback: (Boolean) -> Unit
) : RecyclerView.OnScrollListener() {

    private var isShowed = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        changeVisibility(recyclerView, dy)
    }

    private fun changeVisibility(recyclerView: RecyclerView, dy: Int) {
        (recyclerView.layoutManager as? LinearLayoutManager)?.let { linearLayoutManager ->
            val show = dy < 0 &&
                    linearLayoutManager.findFirstVisibleItemPosition() > minimalFirstVisiblePosition &&
                    linearLayoutManager.findLastVisibleItemPosition() != linearLayoutManager.itemCount - 1
            if (show != isShowed) {
                changeVisibilityCallback.invoke(show)
                isShowed = show
            }
        }
    }

}
