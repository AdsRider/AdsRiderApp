package com.capstone.adsrider.service

import com.capstone.adsrider.model.LoginBody
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager

val okHttpClient = OkHttpClient.Builder()
    .cookieJar(JavaNetCookieJar(CookieManager()))
    .build()

val adsRiderRetrofit = Retrofit.Builder()
    .baseUrl("https://adsrider.wo.tc/api/")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object AdsRiderObject {
    val retrofitService: AdsRiderInterface by lazy {
        adsRiderRetrofit.create(AdsRiderInterface::class.java)
    }
}

class AdsRiderService {
    suspend fun login(email: String, password: String) = AdsRiderObject.retrofitService.login(
        LoginBody(email, password)
    )

    suspend fun signIn(email: String, password: String) = AdsRiderObject.retrofitService.signin(
        LoginBody(email, password)
    )

    suspend fun getMe() = AdsRiderObject.retrofitService.whoami()

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
