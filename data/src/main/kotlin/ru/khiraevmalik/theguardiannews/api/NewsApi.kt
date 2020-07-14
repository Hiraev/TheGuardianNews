package ru.khiraevmalik.theguardiannews.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.khiraevmalik.theguardiannews.model.ResponseWrapper

interface NewsApi {

    @GET(Api.TheGuardian.Path.search)
    suspend fun fetchNews(
            @Query(Api.TheGuardian.Params.page) page: Int,
            @Query(Api.TheGuardian.Params.pageSize) pageSize: Int,
            @Query(Api.TheGuardian.Params.orderBy) order: String
    ): ResponseWrapper

    @GET(Api.TheGuardian.Path.search)
    suspend fun searchNews(
            @Query(Api.TheGuardian.Params.page) page: Int,
            @Query(Api.TheGuardian.Params.pageSize) pageSize: Int,
            @Query(Api.TheGuardian.Params.query) query: String,
            @Query(Api.TheGuardian.Params.orderBy) order: String
    ): ResponseWrapper

}
