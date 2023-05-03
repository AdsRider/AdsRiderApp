package com.capstone.adsrider.network

import com.capstone.adsrider.model.AdsInfo
import retrofit2.Call
import retrofit2.http.GET

interface AdsListAPI {
    @GET("api/ads")
    fun getAdsList(): Call<List<AdsInfo>>
}
