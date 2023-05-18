package com.capstone.adsrider.main.rentbike

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.capstone.adsrider.utility.App
import com.capstone.adsrider.utility.RidingSharedPreference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdsExposure(
    imageId: Int,
    navController: NavHostController,
    adsExposureViewModel: AdsExposureViewModel = viewModel()
) {
    val result = adsExposureViewModel.result.collectAsState().value

    if (result != null) {
        Dialog(onDismissRequest = {}) {
            Surface(
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Column {
                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        "리워드 지급",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .padding(vertical = 8.dp),
                        fontSize = 16.sp,
                        lineHeight = 17.sp
                    )
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        "ads 코인 ${result.reward} 개 지급",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .padding(vertical = 8.dp),
                        fontSize = 13.sp
                    )
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                            .fillMaxWidth()
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
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("확인", fontSize = 16.sp)
                    }
                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .padding(16.dp)
            .offset(y = (-30).dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://adsrider.wo.tc/api/image/$imageId}"),
            contentDescription = "Ads Design",
            modifier = Modifier
                .height(500.dp)
                .align(Alignment.Center)
        )
        Button(onClick = {
            val resultData = RidingSharedPreference(App.context()).getRidingPrefs()
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            resultData.end_time = current.format(formatter)
            RidingSharedPreference(App.context()).setRidingPrefs(resultData)
            Log.d("최종", resultData.toString())

            val result = adsExposureViewModel.ridingComplete(resultData)
        }) {
            Text(text = "주행완료")
        }
        Row(
            modifier = Modifier
                .background(Color.White)
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    text = "주행속도"
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = "0km/s",
                    fontSize = 20.sp
                )
            }
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    text = "주행거리"
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = "0km",
                    fontSize = 20.sp

                )
            }
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    text = "주행시간"
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = "00:00:00",
                    fontSize = 20.sp
                )
            }
        }
    }
}
