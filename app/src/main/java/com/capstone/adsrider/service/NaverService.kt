package com.capstone.adsrider.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val naverRetrofit = Retrofit.Builder()
    .baseUrl("https://map.naver.com/v5/api")
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
            start, // "126.9820673,37.4853855,name=이수역 7호선",
            destination, // "126.9803409,37.5029146,name=동작역 4호선",
        )
    }.getOrNull()?.routes?.get(0)?.summary

    suspend fun getPlace(destination: String) = runCatching {
        NaverObject.retrofitService.getPlaces(
            coords = "126.9820673,37.4853855", // "126.9820673,37.4853855",
            query = destination, // "동작역 4호선",
        )
    }.getOrNull()?.place
}
