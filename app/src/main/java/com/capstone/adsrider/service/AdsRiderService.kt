package com.capstone.adsrider.service

import com.capstone.adsrider.model.LoginBody
import com.capstone.adsrider.model.ResultBody
import com.capstone.adsrider.model.WithdrawalBody
import com.capstone.adsrider.model.buyTicketBody
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
        LoginBody(null, email, password),
    )

    suspend fun signIn(level: String, email: String, password: String) = AdsRiderObject.retrofitService.signin(
        LoginBody(level, email, password),
    )
    suspend fun logout() = AdsRiderObject.retrofitService.logout()

    suspend fun getMe() = AdsRiderObject.retrofitService.whoami()

    suspend fun getBalance() = AdsRiderObject.retrofitService.getBalance()

    suspend fun getHistory() = AdsRiderObject.retrofitService.getHistory()

    suspend fun getAdsList() = AdsRiderObject.retrofitService.getAdsList()

    suspend fun withdrawal(to: String, amount: String) = AdsRiderObject.retrofitService.withdrawal(
        WithdrawalBody(to, amount),
    )

    suspend fun ridingComplete(body: ResultBody) = AdsRiderObject.retrofitService.adsResult(body)

    suspend fun buyTicket(day: Int) = AdsRiderObject.retrofitService.buyTicket(buyTicketBody(day))
}
