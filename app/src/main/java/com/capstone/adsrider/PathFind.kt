package com.capstone.adsrider

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMapScreen(navController: NavHostController) {
    val seoul = LatLng(37.532600, 127.024612)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(seoul, 11.0)
    }

    var text by remember { mutableStateOf("목적지") }
    Box(Modifier.fillMaxSize()) {
        NaverMap(cameraPositionState = cameraPositionState)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(
                start = 14.dp,
                end = 12.dp,
                top = 11.dp,
                bottom = 11.dp
            )
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
            )
            Button(onClick = {
                // 카메라를 새로운 줌 레벨로 이동합니다.
                cameraPositionState.move(CameraUpdate.zoomIn())
            }) {
                Text(text = "검색")
            }
        }
    }
}
