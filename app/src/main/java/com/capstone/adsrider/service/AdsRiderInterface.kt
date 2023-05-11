package com.capstone.adsrider.service

import com.capstone.adsrider.model.Ad
import com.capstone.adsrider.model.Balance
import com.capstone.adsrider.model.LoginBody
import com.capstone.adsrider.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AdsRiderInterface {
    @GET("user/balance")
    suspend fun getBalance(
        @Query("coords") coords: String,
        @Query("query") query: String
    ): Balance

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

    @GET("user/logout")
    suspend fun logout(
        @Query("email") email: String,
        @Query("password") password: String
    ): User

    @GET("ads")
    suspend fun getAdsList(): List<Ad>

    @POST("user/buyticket")
    suspend fun buyTicket(
        @Body day: Int
    ): User
}
