package ru.khiraevmalik.theguardiannews.presentation.main

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearLayoutManagerRecyclerViewOnScrollListener(
        private val prefetchDistance: Int,
        private val onScrolledCallback: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var enabled = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (enabled) invokeCallbackIfNeeded(recyclerView)
    }

    fun enable() {
        enabled = true
    }

    fun disable() {
        enabled = false
    }

    private fun invokeCallbackIfNeeded(recyclerView: RecyclerView) {
        (recyclerView.layoutManager as? LinearLayoutManager)?.let { linearLayoutManager ->
            val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
            if (lastVisiblePosition != RecyclerView.NO_POSITION && linearLayoutManager.itemCount - lastVisiblePosition < prefetchDistance) {
                onScrolledCallback.invoke()
            }
        }
    }

}
