package ru.khiraevmalik.theguardiannews.presentation.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.khiraevmalik.theguardiannews.R

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val title = view.findViewById<TextView>(R.id.item_news_title)
    private val subtitle = view.findViewById<TextView>(R.id.item_news_subtitle)
    private val image = view.findViewById<ImageView>(R.id.item_news_image)

    fun bind(newsItem: NewsItem) {
        title.text = newsItem.title
        subtitle.text = newsItem.subtitle
        Glide.with(itemView)
                .load(newsItem.imageUrl)
                .into(image)
    }

}
