package com.capstone.adsrider.model

data class User(
    val email: String,
    val level: String,
    val address: String,
    val expire_date: Long,
    val join_time: Long
)
