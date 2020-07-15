package ru.khiraevmalik.theguardiannews.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseBody(
        val status: Status,
        val results: List<ResultBody>
)
