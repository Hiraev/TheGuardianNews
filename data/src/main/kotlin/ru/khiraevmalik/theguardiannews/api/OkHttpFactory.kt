package ru.khiraevmalik.theguardiannews.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.khiraevmalik.theguardiannews.data.BuildConfig
import java.util.concurrent.TimeUnit

object OkHttpFactory {

    private const val CONNECT_TIMEOUT_SECONDS = 10L
    private const val CALL_TIMEOUT_SECONDS = 15L

    fun createOkHttpClient() = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .callTimeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
            }
            .build()

}
