package com.capstone.adsrider.model

import org.threeten.bp.LocalDateTime

data class AdsInfo(
    val id: Int,
    val user_id: String,
    val amount: Double,
    val max_amount: Double,
    val file: Int,
    val create_date: LocalDateTime,
    val close_date: LocalDateTime
)
