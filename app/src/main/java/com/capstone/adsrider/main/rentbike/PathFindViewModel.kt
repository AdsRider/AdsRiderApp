package com.capstone.adsrider.main.rentbike

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.model.NaverPath
import com.capstone.adsrider.service.NaverService
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PathFindViewModel : ViewModel() {
    val naverService = NaverService()

    fun generatePath(naverMap: NaverMap, start: String, destination: String) {
        viewModelScope.launch {
            val routeSummary = naverService.getPath(start, destination)
            drawPath(naverMap, routeSummary!!)
        }
    }

    private fun drawPath(naverMap: NaverMap, summary: NaverPath.RouteSummary): PathOverlay {
        val path = PathOverlay()

        val startLatLng = LatLng(
            summary.start.location.split(",")[0].toDouble(),
            summary.start.location.split(",")[1].toDouble()
        )

        val waypoints = summary.road_summary.map {
            LatLng(
                it.location.split(",")[0].toDouble(),
                it.location.split(",")[1].toDouble()
            )
        }

        val endLatLng = summary.end.location.split(",").let {
            LatLng(it[0].toDouble(), it[1].toDouble())
        }

        val fullPath: List<LatLng> = listOf(startLatLng) + waypoints + endLatLng

        path.coords = fullPath
        path.color = Color.BLUE
        path.map = naverMap

        return path
    }
}
