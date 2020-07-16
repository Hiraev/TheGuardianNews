package ru.khiraevmalik.theguardiannews.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import ru.khiraevmalik.theguardiannews.R
import ru.khiraevmalik.theguardiannews.presentation.main.adapter.view_holders.ErrorLoadingMoreViewHolder
import ru.khiraevmalik.theguardiannews.presentation.main.adapter.view_holders.FullDataViewHolder
import ru.khiraevmalik.theguardiannews.presentation.main.adapter.view_holders.LoadingMoreViewHolder
import ru.khiraevmalik.theguardiannews.presentation.main.adapter.view_holders.NewsViewHolder
import ru.khiraevmalik.theguardiannews.utils.rippleClick

class NewsAdapter(
        private val onRetryButtonClickListener: () -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private enum class NewsItemViewType {
        DATA, LOADING_MORE, LOADING_ERROR, FULL_DATA_ITEM
    }

    private val differ = AsyncListDiffer(this, NewsItemDifferCallback)
    private var itemClickListener: (NewsItem) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                NewsItemViewType.DATA.ordinal -> createNewsItemViewHolder(parent)
                NewsItemViewType.LOADING_MORE.ordinal -> createLoadingMoreViewHolder(parent)
                NewsItemViewType.LOADING_ERROR.ordinal -> createLoadingMoreError(parent)
                NewsItemViewType.FULL_DATA_ITEM.ordinal -> createFullDataItem(parent)
                else -> throw IllegalArgumentException("Unknown viewType: $viewType")
            }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsViewHolder -> bindNewsItemData(holder, position)
        }
    }

    override fun getItemViewType(position: Int): Int = when (differ.currentList[position]) {
        is NewsItem.Data -> NewsItemViewType.DATA.ordinal
        is NewsItem.LoadingMore -> NewsItemViewType.LOADING_MORE.ordinal
        is NewsItem.ErrorLoadingMore -> NewsItemViewType.LOADING_ERROR.ordinal
        is NewsItem.FullDataItem -> NewsItemViewType.FULL_DATA_ITEM.ordinal
    }

    fun setItemClickListener(listener: (NewsItem) -> Unit) {
        itemClickListener = listener
    }

    fun submitList(items: List<NewsItem>) {
        differ.submitList(items)
    }

    private fun createNewsItemViewHolder(parent: ViewGroup) =
            NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false))

    private fun createLoadingMoreViewHolder(parent: ViewGroup) =
            LoadingMoreViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false))

    private fun createFullDataItem(parent: ViewGroup) =
            FullDataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_all_news_loaded, parent, false))

    private fun createLoadingMoreError(parent: ViewGroup) = ErrorLoadingMoreViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_loading_error_with_retry, parent, false),
            onRetryButtonClickListener
    )

    private fun bindNewsItemData(holder: NewsViewHolder, position: Int) {
        holder.bind(differ.currentList[position] as NewsItem.Data)
        holder.itemView.rippleClick { itemClickListener.invoke(differ.currentList[position]) }
    }

}
