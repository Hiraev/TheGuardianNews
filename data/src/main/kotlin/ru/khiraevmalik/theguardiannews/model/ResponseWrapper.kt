package ru.khiraevmalik.theguardiannews.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseWrapper(
        val response: ResponseBody
)
