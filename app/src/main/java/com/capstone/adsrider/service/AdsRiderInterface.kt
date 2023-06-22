package com.capstone.adsrider.service

import com.capstone.adsrider.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AdsRiderInterface {
    @GET("user/balance")
    suspend fun getBalance(): List<Balance>

    @GET("user/dw/history")
    suspend fun getHistory(): List<History>

    @POST("user/withdrawal")
    suspend fun withdrawal(
        @Body withdrawalBody: WithdrawalBody,
    ): String

    @POST("payment/success")
    suspend fun purchaseCoin(
        @Body purchaseCoinBody: PurchaseCoinBody,
    ): Balance

    @POST("ads/result")
    suspend fun adsResult(
        @Body resultBody: ResultBody,
    ): ResultResponse

    @GET("user/me")
    suspend fun whoami(): User

    @POST("user/signin")
    suspend fun signin(
        @Body loginBody: LoginBody,
    ): User

    @POST("user/login")
    suspend fun login(
        @Body loginBody: LoginBody,
    ): User

    @GET("user/logout")
    suspend fun logout()

    @GET("ads")
    suspend fun getAdsList(): List<Ad>

    @POST("user/buyticket")
    suspend fun buyTicket(
        @Body buyTicketBody: buyTicketBody
    ): User

    @GET("statistics")
    suspend fun getStatistic(
        @Query("from") fromDate: String,
        @Query("to") toDate: String
    ): List<Statistic>
}
