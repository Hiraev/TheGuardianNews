package ru.khiraevmalik.theguardiannews.presentation.main.adapter

import androidx.recyclerview.widget.DiffUtil

object NewsItemDifferCallback : DiffUtil.ItemCallback<NewsItem>() {

    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = when {
        oldItem is NewsItem.ErrorLoadingMore && newItem is NewsItem.ErrorLoadingMore ||
                oldItem is NewsItem.LoadingMore && newItem is NewsItem.LoadingMore ||
                oldItem is NewsItem.FullDataItem && newItem is NewsItem.FullDataItem -> true
        oldItem is NewsItem.Data && newItem is NewsItem.Data -> oldItem.id == newItem.id
        else -> false
    }

    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = when {
        oldItem is NewsItem.ErrorLoadingMore && newItem is NewsItem.ErrorLoadingMore ||
                oldItem is NewsItem.LoadingMore && newItem is NewsItem.LoadingMore ||
                oldItem is NewsItem.FullDataItem && newItem is NewsItem.FullDataItem -> true
        oldItem is NewsItem.Data && newItem is NewsItem.Data -> areDataContentsTheSame(oldItem, newItem)
        else -> false
    }

    private fun areDataContentsTheSame(oldItem: NewsItem.Data, newItem: NewsItem.Data) =
            oldItem.title == newItem.title && oldItem.subtitle == newItem.subtitle && oldItem.imageUrl == newItem.imageUrl

}
