package com.capstone.adsrider.model

data class ResultBody(
    var ads_id: String,
    var meters: Int,
    var path: String,
    var start_time: String,
    var end_time: String
)

data class ResultResponse(
    var reward: String,
    var hash: String
)
