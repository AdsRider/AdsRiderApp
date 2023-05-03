package com.capstone.adsrider.model

import com.capstone.adsrider.network.AdsListAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class AdsInfo(
    val id: Int,
    val title: String,
    val subtitle: String,
    val reward: String,
    val image_id: Number,
    val start_date: Number,
    val end_date: Number,
    val user_email: String
)

val adsRetrofit = Retrofit.Builder()
    .baseUrl("https://adsrider.wo.tc/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val adsRetrofitService = adsRetrofit.create(AdsListAPI::class.java)
val adsList = adsRetrofitService.getAdsList()
