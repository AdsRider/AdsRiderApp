package com.capstone.adsrider.service

import com.capstone.adsrider.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AdsRiderInterface {
    @GET("user/balance")
    suspend fun getBalance(): List<Balance>

    @GET("user/dw/history")
    suspend fun getHistory(): List<History>

    @POST("user/withdrawal")
    suspend fun withdrawal(
        @Body withdrawalBody: WithdrawalBody
    ): String

    @POST("user/ridingcomplete")
    suspend fun ridingComplete(
        @Body riding: Riding
    ): String

    @GET("user/me")
    suspend fun whoami(): User

    @POST("user/signin")
    suspend fun signin(
        @Body loginBody: LoginBody
    ): User

    @POST("user/login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): User

    @GET("ads")
    suspend fun getAdsList(): List<Ad>

    @POST("user/buyticket")
    suspend fun buyTicket(
        @Body buyTicketBody: buyTicketBody
    ): User
}
