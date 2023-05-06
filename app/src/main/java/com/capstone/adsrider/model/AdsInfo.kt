package com.capstone.adsrider.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class Ad(
    val id: Int,
    val title: String,
    val subtitle: String,
    val reward: String,
    val image_id: Number,
    val start_date: Number,
    val end_date: Number,
    val user_email: String
)
