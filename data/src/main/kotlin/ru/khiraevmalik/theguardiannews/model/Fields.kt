package ru.khiraevmalik.theguardiannews.model

data class Fields(
        val headline: String,
        val trailText: String,
        val thumbnail: String = "",
        val bodyText: String
)
