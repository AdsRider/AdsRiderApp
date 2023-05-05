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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.capstone.adsrider.BottomNavigationGraph
import com.capstone.adsrider.model.AdsInfo
import com.capstone.adsrider.model.adsList
import com.capstone.adsrider.ui.theme.AdsRiderTheme
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

suspend fun getAdsList(): List<AdsInfo> = suspendCoroutine { continuation ->
    adsList.enqueue(object: Callback<List<AdsInfo>> {
        override fun onResponse(call: Call<List<AdsInfo>>, response: Response<List<AdsInfo>>) {
            if (response.isSuccessful) {
                val lst = response.body()!!
                continuation.resume(lst)
            } else {
                continuation.resumeWithException(
                    Throwable("API request failed with response code: ${response.code()}")
                )
            }
        }
        override fun onFailure(call: Call<List<AdsInfo>>, t: Throwable) {
            continuation.resumeWithException(t)
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
fun AdsView(navController: NavHostController) {
    var state by remember { mutableStateOf(false) }
    var courseList by remember { mutableStateOf<List<AdsInfo>>(emptyList()) }
    var index by remember { mutableStateOf<Int>(0) }

    LaunchedEffect(Unit) {
        try {
            val lst = getAdsList()
            courseList = lst
        } catch (e: Exception) {
            Log.e("API_ERROR", "API request failed: ${e.message}")
        }
    }
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
                .clickable {
                    state = true
                    index = courseList.indexOf(it)
                }
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
    if (state) {
        BottomSheetDialog(
            onDismissRequest = { state = false },
            properties = BottomSheetDialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                navigationBarColor = MaterialTheme.colors.surface
            )
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)

                ) {
                    Text(
                        style = MaterialTheme.typography.h5,
                        text = courseList[index].title
                    )
                    Text(
                        style = MaterialTheme.typography.body1,
                        text = "광고정보: ${courseList[index].subtitle}\n" + "지급 코인: ${courseList[index].reward}"
                    )
                    Text(
                        style = MaterialTheme.typography.h6,
                        color = Color.LightGray,
                        text = "광고디자인"
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                    Image(
                        painter = rememberAsyncImagePainter("https://adsrider.wo.tc/api/image/${courseList[index].image_id}"),
                        contentDescription = "Ads Design",
                        modifier = Modifier.height(200.dp)
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            navController.navigate("ad exposure/${courseList[index].image_id}")
                        }
                        ) {
                        Text(text = "광고 선택")
                    }
                }
            }
        }
    }
}

@Composable
fun AdsExposure(imageId: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .padding(16.dp)
            .offset(y = (-30).dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://adsrider.wo.tc/api/image/${imageId}}"),
            contentDescription = "Ads Design",
            modifier = Modifier
                .height(500.dp)
                .align(Alignment.Center)
        )
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .align(Alignment.BottomCenter)
            ) {
                Column(modifier = Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        text = "주행속도",
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = "30km/s",
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
                        text = "30km",
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
                        text = "01:10:02",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }

