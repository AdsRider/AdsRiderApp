package com.capstone.adsrider.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val adsRiderRetrofit = Retrofit.Builder()
    .baseUrl("https://adsrider.wo.tc/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object AdsRiderObject {
    val retrofitService: AdsRiderInterface by lazy {
        adsRiderRetrofit.create(AdsRiderInterface::class.java)
    }
}

class AdsRiderService {
    suspend fun login(email: String, password: String) = runCatching {
        AdsRiderObject.retrofitService.login(
            email,
            password
        )
    }.getOrNull()

    suspend fun signin(email: String, password: String) = runCatching {
        AdsRiderObject.retrofitService.signin(
            email,
            password
        )
    }.getOrNull()

    suspend fun logout(email: String, password: String) = runCatching {
        AdsRiderObject.retrofitService.logout(
            email,
            password
        )
    }.getOrNull()

    suspend fun getBalance(email: String, password: String) = runCatching {
        AdsRiderObject.retrofitService.getBalance(
            email,
            password
        )
    }.getOrNull()

    suspend fun getAdsList() = AdsRiderObject.retrofitService.getAdsList()

    suspend fun buyTicket(day: Int) = runCatching {
        AdsRiderObject.retrofitService.buyTicket(day)
    }.getOrNull()
}
