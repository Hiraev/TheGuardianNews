package ru.khiraevmalik.theguardiannews.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Fields(
        val headline: String,
        val trailText: String,
        val thumbnail: String = "",
        val bodyText: String
)
