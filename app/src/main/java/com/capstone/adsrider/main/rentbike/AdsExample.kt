package com.capstone.adsrider.main.rentbike

import com.capstone.adsrider.R
import com.capstone.adsrider.model.AdsInfo
import org.threeten.bp.LocalDateTime

class AdsExample {
    val google: AdsInfo = AdsInfo(
        10001, "google", 1.2, 2.0, R.drawable.icon_google,
        LocalDateTime.of(2023, 4, 11, 14, 22, 32),
        LocalDateTime.of(2023, 4, 30, 14, 22, 31)
    )
    val tukorea: AdsInfo = AdsInfo(
        10002, "tukorea", 1.0, 1.3, R.drawable.tukorea,
        LocalDateTime.of(2023, 4, 3, 10, 8, 51),
        LocalDateTime.of(2023, 5, 3, 10, 8, 50)
    )
}
