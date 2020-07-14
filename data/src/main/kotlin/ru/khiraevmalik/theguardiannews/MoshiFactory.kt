package ru.khiraevmalik.theguardiannews

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiFactory {

    fun createMoshiForTheGuardianApi(): Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

}
