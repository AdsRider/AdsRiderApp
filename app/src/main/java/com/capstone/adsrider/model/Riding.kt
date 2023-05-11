package com.capstone.adsrider.model

data class Riding(
    var ads_id: String,
    var distance: Long,
    var path: String,
    var start_at: Long,
    var completed_at: Long
)
