package ru.khiraevmalik.theguardiannews.api

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitFactory {

    fun createTheGuardianRetrofit(client: OkHttpClient, moshi: Moshi) = Retrofit.Builder()
            .baseUrl(Api.TheGuardian.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

    fun createNewsApi(retrofit: Retrofit) = retrofit.create(NewsApi::class.java)

}
