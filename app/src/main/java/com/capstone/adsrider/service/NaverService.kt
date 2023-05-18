package com.capstone.adsrider.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val naverRetrofit = Retrofit.Builder()
    .baseUrl("https://map.naver.com/v5/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object NaverObject {
    val retrofitService: NaverInterface by lazy {
        naverRetrofit.create(NaverInterface::class.java)
    }
}

class NaverService {
    suspend fun getPath(start: String, destination: String) = runCatching {
        NaverObject.retrofitService.getPath(
            start,
            destination
        )
    }.getOrNull()?.routes?.get(0)?.legs?.get(0)

    suspend fun getPlaces(start: String, word: String) = runCatching {
        NaverObject.retrofitService.getPlaces(
            coords = start,
            query = word
        )
    }.getOrNull()?.place
}
