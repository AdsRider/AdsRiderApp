package com.capstone.adsrider.main.rentbike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.model.ResultBody
import com.capstone.adsrider.model.ResultResponse
import com.capstone.adsrider.service.AdsRiderService
import com.capstone.adsrider.service.NaverService
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AdsExposureViewModel : ViewModel() {
    val naverService = NaverService()
    private val adsRiderService = AdsRiderService()
    private val _result = MutableStateFlow<ResultResponse?>(null)
    private val _drivingTime = MutableStateFlow(0)
    private val _path = MutableStateFlow(emptyList<LatLng>())
    private val _distance = MutableStateFlow(0)

    val path get() = _path
    val distance get() = _distance
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

    fun getPath(start: LatLng, end: LatLng) {
        viewModelScope.launch {
            val startParam = "${start.longitude},${start.latitude},name="
            val destinationParam = "${end.longitude},${end.latitude},name="

            val pathData = naverService.getPath(startParam, destinationParam)!!

            val startLatLng = pathData.summary.start.location.split(",").let {
                LatLng(it[1].toDouble(), it[0].toDouble())
            }

            val waypoints = pathData.steps.map {
                LatLng(
                    it.guide.turn_point.split(",")[1].toDouble(),
                    it.guide.turn_point.split(",")[0].toDouble()
                )
            }

            val endLatLng = pathData.summary.end.location.split(",").let {
                LatLng(it[1].toDouble(), it[0].toDouble())
            }

            _path.value = listOf(startLatLng) + waypoints + endLatLng
            _distance.value = pathData.summary.distance.toInt()
        }
    }
}
