package ru.khiraevmalik.theguardiannews.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResultBody(
        val id: String,
        val fields: Fields
)
