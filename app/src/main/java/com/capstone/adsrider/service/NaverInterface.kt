package com.capstone.adsrider.service

import com.capstone.adsrider.model.NaverPath
import com.capstone.adsrider.model.NaverPlace
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverInterface {
    @GET("/dir/findbicycle")
    suspend fun getPath(
        @Query("start") start: String,
        @Query("goal") goal: String
    ): NaverPath

    @GET("/instantSearch?")
    suspend fun getPlaces(
        @Query("coords") coords: String,
        @Query("query") query: String,
    ): NaverPlace
}
