package ru.khiraevmalik.theguardiannews.presentation.main.adapter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class NewsItem {

    @Parcelize
    data class Data(
            val id: String,
            val title: String,
            val subtitle: String,
            val imageUrl: String,
            val body: String
    ) : NewsItem(), Parcelable

    object LoadingMore : NewsItem()
    object ErrorLoadingMore : NewsItem()
    object FullDataItem : NewsItem()
}
