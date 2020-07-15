package ru.khiraevmalik.theguardiannews.presentation.main

import android.os.SystemClock
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollUpButtonVisibilityOnScrollListener(
        private val minimalFirstVisiblePosition: Int,
        private val changeVisibilityCallback: (Boolean) -> Unit
) : RecyclerView.OnScrollListener() {

    private var lastCalledTime = 0L

    companion object {
        // Don't call too often, must be less then animation time
        private const val MIN_DELAY_BETWEEN_CALLS = 500L
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        changeVisibility(recyclerView, dy)
    }

    private fun changeVisibility(recyclerView: RecyclerView, dy: Int) {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastCalledTime >= MIN_DELAY_BETWEEN_CALLS) {
            (recyclerView.layoutManager as? LinearLayoutManager)?.let { linearLayoutManager ->
                val firstVisible = linearLayoutManager.findFirstVisibleItemPosition()
                val show = firstVisible > minimalFirstVisiblePosition && dy < 0
                changeVisibilityCallback.invoke(show)
            }
            lastCalledTime = currentTime
        }
    }

}
