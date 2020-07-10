package ru.khiraevmalik.theguardiannews.utils

import android.os.SystemClock

object RippleActionInvoker {

    const val DEFAULT_RIPPLE_DELAY = 300L
    private var lastActionTime = 0L

    fun rippleAction(delay: Long = DEFAULT_RIPPLE_DELAY, action: () -> Unit) {
        val currentTime = SystemClock.elapsedRealtime()
        val diff = currentTime - lastActionTime
        if (diff >= delay) {
            lastActionTime = currentTime
            action.invoke()
        }
    }

}
