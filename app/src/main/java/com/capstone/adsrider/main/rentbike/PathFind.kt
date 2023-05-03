package com.capstone.adsrider.main.rentbike

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.capstone.adsrider.BottomNavigationGraph
import com.capstone.adsrider.model.AdsInfo
import com.capstone.adsrider.model.adsList
import com.capstone.adsrider.ui.theme.AdsRiderTheme
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

fun getAdsList(courseList: MutableList<AdsInfo>) {
    adsList.enqueue(object: Callback<List<AdsInfo>> {
        override fun onResponse(call: Call<List<AdsInfo>>, response: Response<List<AdsInfo>>) {
            if (response.isSuccessful) {
                val lst = response.body()!!
                lst.forEach {
                    courseList.add(it)
                }
            }
        }
        override fun onFailure(call: Call<List<AdsInfo>>, t: Throwable) {
            Log.e("API_ERROR", "Network request failed: ${t.message}")
        }
    })
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AdsView() {
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val courseList = remember { mutableListOf<AdsInfo>() }
    getAdsList(courseList)
    ModalBottomSheetLayout(
        sheetState = state,
        sheetShape = RoundedCornerShape(20.dp),
        sheetContent = {
            LazyColumn(modifier = Modifier.height(200.dp)) {
                items(courseList) {
                    Text(
                        style = MaterialTheme.typography.h5,
                        text = it.title
                    )
                    Text(
                        style = MaterialTheme.typography.body1,
                        text = "광고정보: ${it.subtitle}\n" + "지급 코인: ${it.reward}"
                    )
                    Text(
                        style = MaterialTheme.typography.h6,
                        color = Color.LightGray,
                        text = "광고디자인"
                    )
                    Image(
                        painter = rememberAsyncImagePainter("https://adsrider.wo.tc/api/image/${it.image_id}"),
                        contentDescription = "Ads Design"
                    )
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.LightGray)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 20.dp),
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    text = "광고 선택"
                )
            }
            items(courseList) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 70.dp)
                    .clickable { scope.launch { state.show() } }
                ) {
                    Text(
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Left,
                        text = it.title
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Right,
                        text = "${it.reward} ads"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun preview() {
    AdsRiderTheme {
        AdsView()
    }
}
