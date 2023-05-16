package com.capstone.adsrider.main.pathfind

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.capstone.adsrider.R
import com.capstone.adsrider.main.rentbike.PathFindViewModel
import com.capstone.adsrider.model.Riding
import com.capstone.adsrider.utility.App
import com.capstone.adsrider.utility.CalDistance
import com.capstone.adsrider.utility.RidingSharedPreference
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage

private lateinit var fusedLocationClient: FusedLocationProviderClient

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun PathFindScreen(navController: NavHostController, pathFindViewModel: PathFindViewModel = viewModel()) {
    var startPosition by remember { mutableStateOf(LatLng(37.3400333, 126.7335056)) }
    // 위치 권한 요청 및 현재 위치 구하기
    val context = LocalContext.current

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO
        // 나중에 기재 예정
    } else {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location ->
                startPosition = LatLng(location.latitude, location.longitude)
                Log.d("pos", startPosition.toString())
            }
    }
    val path = pathFindViewModel.path.collectAsState().value
    val places = pathFindViewModel.places.collectAsState().value
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(startPosition, 11.0)
    }

    Log.d("places", places.toString())
    Log.d("path", path.toString())
    var desWord by remember { mutableStateOf("") }

    @Composable
    fun PlacesView() {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            items(places) { place ->
                Card(
                    modifier = Modifier.width(400.dp),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(size = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp)
                            .clickable {
                                pathFindViewModel.getPath(startPosition, place)
                                desWord = place.title
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = place.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun NavigationView() {
        val startPosition = LatLng(path.first().latitude, path.first().longitude)
        val destinationPosition = LatLng(path.last().latitude, path.last().longitude)
        val middlePosition = LatLng(
            (startPosition.latitude + destinationPosition.latitude) / 2,
            (startPosition.longitude + destinationPosition.longitude) / 2
        )

        cameraPositionState.position = CameraPosition(middlePosition, 13.0)

        Marker(
            state = MarkerState(position = startPosition),
            captionText = "출발지"
        )
        Marker(
            state = MarkerState(position = destinationPosition),
            captionText = "도착지"
        )

        PathOverlay(
            coords = path,
            color = Color.Blue,
            width = 12.dp,
            patternImage = OverlayImage.fromResource(R.drawable.icon_navigator),
            patternInterval = 10.dp
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            if (path.isNotEmpty()) {
                NavigationView()
            }
        }
        Column(
            modifier = Modifier
                .width(350.dp)
                .align(Alignment.TopCenter)
                .padding(top = 30.dp)
        ) {
            Row {
                TextField(
                    modifier = Modifier.height(60.dp),
                    value = desWord,
                    singleLine = true,
                    onValueChange = { desWord = it }
                )
                Button(
                    modifier = Modifier.height(60.dp),
                    onClick = {
                        pathFindViewModel.getPlaces(startPosition, desWord)
                    }
                ) {
                    Text("검색")
                }
            }
            if (places.isNotEmpty()) {
                PlacesView()
            }
        }
    }

    Box(modifier = Modifier.fillMaxHeight(), Alignment.BottomEnd) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                var gson = Gson()
                var calDistance = CalDistance()
                var distance = 0.0
                lateinit var before: LatLng
                path.forEachIndexed{ index, item ->
                    if (index != 0) {
                        distance += calDistance.getDistance(before, path[index])
                    }
                    before = path[index]
                }

                val ridingData = Riding(
                    ads_id = "",
                    distance = distance.toLong(),
                    path = gson.toJson(path),
                    start_at = 0,
                    completed_at = 0
                )
                RidingSharedPreference(App.context()).setRidingPrefs(ridingData)
                navController.navigate("ad select")
            }
        ) {
            Text(text = "광고 선택")
        }
    }
}
