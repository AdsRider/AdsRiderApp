package com.capstone.adsrider.utility

import com.naver.maps.geometry.LatLng

class CalDistance() {
    var theta: Double = 0.0
    var dist: Double = 0.0

    fun getDistance(before: LatLng, current: LatLng): Double {
        theta = before.longitude - current.longitude
        dist = Math.sin(deg2rad(before.latitude)) * Math.sin(deg2rad(current.latitude))
        +Math.cos(deg2rad(before.latitude)) * Math.cos(deg2rad(current.latitude)) * Math.cos(deg2rad(theta))

        dist = Math.acos(dist)

        dist = rad2deg(dist)

        dist *= 60 * 1.1515
        dist *= 1.609344; // 단위 mile 에서 km 변환.
        dist *= 1000.0; // 단위  km 에서 m 로 변환

        return dist; // 단위 m
    }

    // 주어진 도(degree) 값을 라디언으로 변환
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}
