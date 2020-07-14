package ru.khiraevmalik.theguardiannews.model

data class ResponseBody(
        val status: Status,
        val results: List<ResultBody>
)
