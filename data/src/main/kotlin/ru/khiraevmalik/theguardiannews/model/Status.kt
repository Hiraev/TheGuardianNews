package ru.khiraevmalik.theguardiannews.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class Status {
    @Json(name = "ok")
    OK,

    @Json(name = "error")
    ERROR
}
