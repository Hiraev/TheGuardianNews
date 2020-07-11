package ru.khiraevmalik.theguardiannews.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.khiraevmalik.theguardiannews.R
import ru.khiraevmalik.theguardiannews.utils.rippleClick

class NewsAdapter : RecyclerView.Adapter<NewsViewHolder>() {

    companion object {
        private val CALLBACK = object : DiffUtil.ItemCallback<NewsItem>() {

            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean =
                    oldItem.title == newItem.title && oldItem.subtitle == newItem.subtitle && oldItem.imageUrl == newItem.imageUrl

        }
    }

    private val differ = AsyncListDiffer(this, CALLBACK)
    private var itemClickListener: (NewsItem) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
            NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false))

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.itemView.rippleClick { itemClickListener.invoke(differ.currentList[position]) }
    }

    fun setItemClickListener(listener: (NewsItem) -> Unit) {
        itemClickListener = listener
    }

    fun submitList(items: List<NewsItem>) {
        differ.submitList(items)
    }

}
