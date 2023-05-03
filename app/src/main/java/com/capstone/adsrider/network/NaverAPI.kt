package com.capstone.adsrider.network

import com.capstone.adsrider.model.ResultPath
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverAPI {
    @GET("v5/api/dir/findbicycle")
    fun getPath(
        @Query("start") start: String,
        @Query("goal") goal: String
    ): Call<ResultPath>
}
