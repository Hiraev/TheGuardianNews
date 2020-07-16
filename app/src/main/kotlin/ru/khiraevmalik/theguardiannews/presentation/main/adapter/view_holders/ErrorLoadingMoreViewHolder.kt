package ru.khiraevmalik.theguardiannews.presentation.main.adapter.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_loading_error_with_retry.view.item_loading_more_error_retry_button
import ru.khiraevmalik.theguardiannews.utils.rippleClick

class ErrorLoadingMoreViewHolder(view: View, onRetryButtonClickListener: () -> Unit) : RecyclerView.ViewHolder(view) {

    init {
        view.item_loading_more_error_retry_button.rippleClick {
            onRetryButtonClickListener.invoke()
        }
    }

}
