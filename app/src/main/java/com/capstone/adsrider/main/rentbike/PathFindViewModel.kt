package com.capstone.adsrider.main.rentbike

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.model.NaverPlace
import com.capstone.adsrider.service.NaverService
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PathFindViewModel : ViewModel() {
    val naverService = NaverService()

    private val _places = MutableStateFlow(emptyList<NaverPlace.Place>())
    val places get() = _places

    private val _path = MutableStateFlow(emptyList<LatLng>())
    val path get() = _path

    private val _distance = MutableStateFlow(0)
    val distance get() = _distance

    fun getPlaces(start: LatLng, word: String) {
        viewModelScope.launch {
            val startParam = "${start.latitude},${start.longitude}"
            _places.value = naverService.getPlaces(startParam, word)!!
        }
    }

    fun getPath(start: LatLng, destination: NaverPlace.Place) {
        viewModelScope.launch {
            val startParam = "${start.longitude},${start.latitude},name="
            val destinationParam = destination.let {
                "${it.x},${it.y},name=${it.title},placeid=${it.cid}"
            }

            Log.d("start", startParam)
            Log.d("destination", destinationParam)

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
            _places.value = emptyList()
            _distance.value = pathData.summary.distance.toInt()
        }
    }
}
