package com.capstone.adsrider.model

data class PurchaseCoinBody(
    val paymentType: String,
    val orderId: String,
    val paymentKey: String,
    val amount: String,
)
