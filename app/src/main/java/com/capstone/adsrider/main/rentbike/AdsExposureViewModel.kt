package com.capstone.adsrider.main.rentbike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.model.ResultBody
import com.capstone.adsrider.model.ResultResponse
import com.capstone.adsrider.service.AdsRiderService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AdsExposureViewModel : ViewModel() {
    private val adsRiderService = AdsRiderService()
    private val _result = MutableStateFlow<ResultResponse?>(null)
    private val _drivingTime = MutableStateFlow(0)


    val result get() = _result
    val drivingTime get() = _drivingTime

    fun ridingComplete(resultBody: ResultBody) {
        viewModelScope.launch {
            result.value = adsRiderService.ridingComplete(resultBody)
        }
    }
    fun runDrivingTime() {
        viewModelScope.launch {
            delay(1000L)
            _drivingTime.value += 1
        }
    }
}
