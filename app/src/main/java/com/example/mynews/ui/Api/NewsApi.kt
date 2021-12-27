package com.example.mynews.ui.Api

import com.example.mynews.ui.models.NewsResponse
import com.example.mynews.ui.util.Constants.Companion.api_Key
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun breakingnews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = api_Key,
    ): Response<NewsResponse>

    @GET("v2/top-everything")
    suspend fun searchfornews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = api_Key,
    ): Response<NewsResponse>
}