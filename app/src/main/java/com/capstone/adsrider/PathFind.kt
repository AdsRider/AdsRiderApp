package com.capstone.adsrider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.adsrider.ui.theme.AdsRiderTheme
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap

class PathFind : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdsRiderTheme {
                PathFindScreen()
            }
        }
    }
}

@Composable
fun PathFindScreen() {
    var navController = rememberNavController()
    Scaffold(bottomBar = {
        Button(
            onClick = {navController.navigate("ad select")},
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "광고 선택")
        }
    }) {
        Box(Modifier.padding(it)) {
            BottomNavigationGraph(navController = navController)
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMapScreen(navController: NavHostController) {
    var text by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        NaverMap(
            modifier = Modifier.fillMaxSize()
        )
        Surface(
            modifier = Modifier
                .width(250.dp)
                .align(Alignment.TopCenter)
                .padding(top = 150.dp)
                .background(Color.LightGray)
                .height(80.dp)
        ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it }
            )
        }
    }
    Box(modifier = Modifier.fillMaxHeight(), Alignment.BottomEnd) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("ad select") }) {
            Text(text = "광고 선택")
        }
    }
}

@Composable
fun AdsView(navController: NavHostController) {
    val scrollState = rememberScrollState()
    val adsExample = AdsExample()
    val ads = listOf(
        adsExample.google,
        adsExample.tukorea
    )
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {
        Text(modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            text = "광고 선택")
        ads.forEach {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .border(width = 4.dp, color = Color.Black)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.size(20.dp))
                    Image(
                        painter = painterResource(id = it.file),
                        contentDescription = "Ads Image", Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Column {
                        Text(text = it.user_id, fontSize = 30.sp)
                        Text(
                            fontSize = 20.sp, text = "리워드:\t${it.amount} ads\n" +
                                "기한:\t\t${String.format("%02d", it.close_date.monthValue)}월" +
                                "${String.format("%02d", it.close_date.dayOfMonth)}일" +
                                "${String.format("%02d", it.close_date.hour)}:" +
                                "${String.format("%02d", it.close_date.minute)}까지"
                        )
                    }
                }
            }
        }
    }
}
