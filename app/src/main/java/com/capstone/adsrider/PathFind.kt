package com.capstone.adsrider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.capstone.adsrider.ui.theme.AdsRiderTheme
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap

class PathFind : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdsRiderTheme {
                NaverMapScreen()
            }
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMapScreen() {
    NaverMap(
        modifier = Modifier.fillMaxSize()
    ) {
        val callgetPath = ApiObject.retrofitService.getPath("126.7335061, 37.3400342", "126.7433065,37.3516988")
    }
}
