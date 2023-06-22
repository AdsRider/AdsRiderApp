package com.capstone.adsrider.main.rentbike

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.capstone.adsrider.utility.App
import com.capstone.adsrider.utility.RidingSharedPreference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.naver.maps.geometry.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdsExposure(
    imageId: Int,
    navController: NavHostController,
    adsExposureViewModel: AdsExposureViewModel = viewModel(),
) {
    var myLocation by remember { mutableStateOf(LatLng(37.3400333, 126.7335056)) }
    val gson = Gson()
    val resultData = RidingSharedPreference(App.context()).getRidingPrefs()
    val context = LocalContext.current
    val activity = context as Activity
    // val ticks = adsExposureViewModel.drivingTime.collectAsState().value
    val result = adsExposureViewModel.result.collectAsState().value
    val path = adsExposureViewModel.path.collectAsState().value
    val distance = adsExposureViewModel.distance.collectAsState().value
    Log.d("result", result.toString())

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    val locationManager = LocalContext.current.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val currentSpeed = remember { mutableStateOf(0) }

    val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // 위치가 변경되면 호출되는 콜백
            val speed = location.speed * 3.6f // m/s를 km/h로 변환
            currentSpeed.value = speed.toInt()
            myLocation = LatLng(location.latitude, location.longitude)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    val configuration = LocalConfiguration.current
    if (result == null) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Log.d("방향", "Landscape")
            }

            else -> {
                Log.d("방향", "Portrait")
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
    }

    DisposableEffect(Unit) {
        // Composable이 나타날 때 위치 업데이트 리스너를 등록
        val minTime = 1000L // 위치 업데이트 간격(ms)
        val minDistance = 0f // 위치 업데이트 최소 거리(m)
        val locationProvider = LocationManager.GPS_PROVIDER // GPS 위치 제공자
        locationManager.requestLocationUpdates(locationProvider, minTime, minDistance, locationListener)

        onDispose {
            // Composable이 사라질 때 위치 업데이트 리스너를 제거
            locationManager.removeUpdates(locationListener)
        }
    }
    // adsExposureViewModel.runDrivingTime()

    if (result != null) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        Dialog(onDismissRequest = {}) {
            Surface(
                modifier = Modifier
                    .height(500.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
            ) {
                Column {
                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                            .fillMaxWidth(),
                    )
                    Text(
                        "리워드 지급",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .padding(vertical = 8.dp),
                        fontSize = 16.sp,
                        lineHeight = 17.sp,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                            .fillMaxWidth(),
                    )
                    Text(
                        "ads 코인 ${result.reward} 개 지급",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .padding(vertical = 8.dp),
                        fontSize = 13.sp,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                            .fillMaxWidth(),
                    )
                    Button(
                        onClick = {
                            navController.navigate("rent bike") {
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(24.dp),
                    ) {
                        Text("확인", fontSize = 16.sp)
                    }
                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }

    if (result == null && distance != 0) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        resultData.end_time = current.format(formatter)
        resultData.path = gson.toJson(path)
        resultData.meters = distance
        RidingSharedPreference(App.context()).setRidingPrefs(resultData)

        Log.d("최종", resultData.toString())
        adsExposureViewModel.ridingComplete(resultData)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .padding(16.dp),
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://adsrider.wo.tc/api/image/$imageId}"),
            contentDescription = "Ads Design",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
        )
        Button(onClick = {
            val gson = Gson()
            val itemType = object : TypeToken<List<LatLng>>() {}.type
            val pathList = gson.fromJson<List<LatLng>>(resultData.path, itemType)
            adsExposureViewModel.getPath(pathList[0], myLocation)
        }) {
            Text(text = "주행완료")
        }
//        Row(
//            modifier = Modifier
//                .background(Color.White)
//                .width(300.dp)
//                .align(Alignment.BottomCenter),
//        ) {
//            Column(
//                modifier = Modifier.weight(1F),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Text(
//                    color = Color.LightGray,
//                    textAlign = TextAlign.Center,
//                    text = "주행속도",
//                )
//                Text(
//                    textAlign = TextAlign.Center,
//                    text = "%dkm/h".format(currentSpeed.value),
//                    fontSize = 20.sp,
//                )
//            }
//            Column(
//                modifier = Modifier.weight(1F),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Text(
//                    color = Color.LightGray,
//                    textAlign = TextAlign.Center,
//                    text = "주행시간",
//                )
////                Text(
////                    textAlign = TextAlign.Center,
////                    text = "%02d:%02d:%02d".format(ticks / 3600, ticks / 60 % 60, ticks % 60),
////                    fontSize = 20.sp,
////                )
//            }
//        }
    }
}
