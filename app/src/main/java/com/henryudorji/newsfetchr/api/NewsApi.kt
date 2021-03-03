package com.henryudorji.newsfetchr.api

import com.henryudorji.newsfetchr.model.NewsResponse
import com.henryudorji.newsfetchr.utils.Constants.Companion.API_KEY
import com.henryudorji.newsfetchr.utils.SharedPref
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//
// Created by  on 2/25/2021.
//
interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("page")
        page: Int = 1,
        @Query("country")
        country: String = SharedPref.getCountryFromPref(),
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("category")
        category: String = SharedPref.getCategoryFromPref()
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        page: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY,
    ): Response<NewsResponse>
}