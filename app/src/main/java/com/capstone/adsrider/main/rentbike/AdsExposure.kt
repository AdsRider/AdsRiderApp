package com.capstone.adsrider.main.rentbike

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.capstone.adsrider.utility.App
import com.capstone.adsrider.utility.RidingSharedPreference

@Composable
fun AdsExposure(imageId: Int, adsExposureViewModel: AdsExposureViewModel = viewModel()) {
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
            val ridingData = RidingSharedPreference(App.context()).getRidingPrefs()
            ridingData.completed_at = System.currentTimeMillis()
            RidingSharedPreference(App.context()).setRidingPrefs(ridingData)
            Log.d("최종", ridingData.toString())
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
