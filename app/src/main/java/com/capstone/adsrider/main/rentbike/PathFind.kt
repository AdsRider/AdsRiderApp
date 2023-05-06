package com.capstone.adsrider.main.rentbike

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.adsrider.BottomNavigationGraph
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
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        Button(
            onClick = { navController.navigate("ad select") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "광고 선택")
        }
    }) {
        Box(Modifier.padding(it)) {
            BottomNavigationGraph(navController = navController)
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMapScreen(navController: NavHostController) {
    val viewModel = PathFindViewModel()
    var text by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        NaverMap(
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .width(350.dp)
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
        ) {
            Row {
                TextField(
                    modifier = Modifier.height(60.dp),
                    value = text,
                    onValueChange = { text = it }
                )
                Button(
                    modifier = Modifier.height(60.dp),
                    onClick = { /*TODO*/ }) {
                    Text("검색")
                }
            }
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
