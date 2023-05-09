package com.capstone.adsrider.main.rentbike

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import kotlinx.coroutines.delay

@Composable
fun AdsView(navController: NavHostController, adsViewModel: AdsViewModel = viewModel()) {
    val ads by adsViewModel.ads.collectAsState()
    var state by remember { mutableStateOf(false) }
    var index by remember { mutableStateOf<Int>(0) }

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
        items(ads) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 70.dp)
                .clickable {
                    state = true
                    index = ads.indexOf(it)
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
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp
            )
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
                        text = ads[index].title
                    )
                    Text(
                        style = MaterialTheme.typography.body1,
                        text = "광고정보: ${ads.get(index).subtitle}\n" + "지급 코인: ${ads[index].reward}"
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
                        painter = rememberAsyncImagePainter(
                            "https://adsrider.wo.tc/api/image/${ads[index].image_id}"
                        ),
                        contentDescription = "Ads Design",
                        modifier = Modifier.height(200.dp)
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            navController.navigate("ad exposure/${ads[index].image_id}")
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
    var time by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(100L)
            time += time
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
            Column(
                modifier = Modifier.weight(1F),
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
                    text = "00:01:13",
                    fontSize = 20.sp
                )
            }
        }
    }
}
//${time / 360}:${time / 60 % 60}:${time % 360}