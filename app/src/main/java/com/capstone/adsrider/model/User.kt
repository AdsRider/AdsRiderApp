package com.capstone.adsrider.model

data class User(
    val email: String,
    val level: String,
    val address: String,
    var expire_date: Long,
    var join_time: Long
)
