package com.capstone.adsrider.main.rentbike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.model.NaverPlace
import com.capstone.adsrider.service.NaverService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PathFindViewModel : ViewModel() {
    val naverService = NaverService()

    private val _places = MutableStateFlow(emptyList<NaverPlace.Place>())
    val places get() = _places

    private val _path = MutableStateFlow(emptyList<LatLng>())
    val path get() = _path

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

            val summary = naverService.getPath(startParam, destinationParam)!!

            val startLatLng = LatLng(
                summary.start.location.split(",")[1].toDouble(),
                summary.start.location.split(",")[0].toDouble()
            )

            val waypoints = summary.road_summary.map {
                LatLng(
                    it.location.split(",")[1].toDouble(),
                    it.location.split(",")[0].toDouble()
                )
            }

            val endLatLng = summary.end.location.split(",").let {
                LatLng(it[1].toDouble(), it[0].toDouble())
            }

            _path.value = listOf(startLatLng) + waypoints + endLatLng
            _places.value = emptyList()
        }
    }
}
