package ru.khiraevmalik.theguardiannews.utils

import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun View.rippleClick(delay: Long = RippleActionInvoker.DEFAULT_RIPPLE_DELAY, action: () -> Unit) {
    setOnClickListener { RippleActionInvoker.rippleAction(delay, action) }
}

fun View.updatePaddings(
        start: Int = paddingStart,
        top: Int = paddingTop,
        end: Int = paddingEnd,
        bottom: Int = paddingBottom
) {
    this.setPadding(start, top, end, bottom)
}

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleWithCheck(visible: Boolean) {
    if (this.isVisible() != visible) visible(visible)
}

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.addSystemTopPadding(targetView: View = this) {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        targetView.updatePaddings(
                top = initialPadding.top + insets.systemWindowInsetTop
        )
        insets.replaceSystemWindowInsets(
                insets.systemWindowInsetLeft,
                0,
                insets.systemWindowInsetRight,
                insets.systemWindowInsetBottom
        )
    }
}

fun View.doOnApplyWindowInsets(block: (View, insets: WindowInsetsCompat, initialPadding: Rect) -> WindowInsetsCompat) {
    val initialPadding = Rect(paddingLeft, paddingTop, paddingRight, paddingBottom)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
    }
    requestApplyInsetsWhenAttached()
}

private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        ViewCompat.requestApplyInsets(this)
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                ViewCompat.requestApplyInsets(v)
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}
