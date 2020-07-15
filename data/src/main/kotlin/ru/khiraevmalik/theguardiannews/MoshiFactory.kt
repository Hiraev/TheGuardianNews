package ru.khiraevmalik.theguardiannews

import com.squareup.moshi.Moshi

object MoshiFactory {

    fun createMoshiForTheGuardianApi(): Moshi = Moshi.Builder()
            .build()

}
