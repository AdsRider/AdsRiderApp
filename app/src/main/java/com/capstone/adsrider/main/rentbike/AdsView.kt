package com.capstone.adsrider.main.rentbike

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.capstone.adsrider.R
import com.capstone.adsrider.utility.App
import com.capstone.adsrider.utility.RidingSharedPreference
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdsView(navController: NavHostController, adsViewModel: AdsViewModel = viewModel()) {
    val ads by adsViewModel.ads.collectAsState()
    var state by remember { mutableStateOf(false) }
    var index by remember { mutableStateOf(0) }
    val filteredAds = ads.filter {
        it.end_date.toDouble() > System.currentTimeMillis() && it.start_date.toDouble() < System.currentTimeMillis()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                style = MaterialTheme.typography.h4,
                color = colorResource(R.color.dark_blue),
                text = "광고 선택",
            )
        }
        items(filteredAds) {
            Row(
                modifier = Modifier
                    .height(100.dp)
                    .clickable {
                        state = true
                        index = ads.indexOf(it)
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.width(100.dp),
                    painter = rememberAsyncImagePainter("https://adsrider.wo.tc/api/image/${it.image_id}"),
                    contentDescription = "Ad image",
                )
                Column {
                    Text(
                        style = MaterialTheme.typography.h5,
                        color = colorResource(R.color.light_blue),
                        text = "  ${it.title}",
                    )
                    Text(
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Right,
                        color = colorResource(R.color.light_blue),
                        text = "  ${it.reward} ads    ",
                    )
                }
            }
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
            )
        }
    }
    if (state) {
        BottomSheetDialog(
            onDismissRequest = { state = false },
            properties = BottomSheetDialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                navigationBarColor = MaterialTheme.colors.surface,
            ),
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),

                ) {
                    Text(
                        style = MaterialTheme.typography.h5,
                        text = ads[index].title,
                    )
                    Text(
                        style = MaterialTheme.typography.body1,
                        text = "광고정보: ${ads[index].subtitle}\n" + "지급 코인: ${ads[index].reward}",
                    )
                    Text(
                        color = Color.LightGray,
                        fontSize = 20.sp,
                        text = "광고디자인",
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.LightGray,
                        thickness = 1.dp,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                "https://adsrider.wo.tc/api/image/${ads[index].image_id}",
                            ),
                            contentDescription = "Ads Design",
                            modifier = Modifier.height(200.dp),
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(R.color.dark_blue),
                        ),
                        onClick = {
                            val resultData = RidingSharedPreference(App.context()).getRidingPrefs()
                            val current = LocalDateTime.now()
                            val formatter = DateTimeFormatter.ISO_DATE_TIME
                            resultData.ads_id = ads[index].id.toString()
                            resultData.start_time = current.format(formatter)
                            RidingSharedPreference(App.context()).setRidingPrefs(resultData)
                            navController.navigate("ad exposure/${ads[index].image_id}")
                        },
                    ) {
                        Text(
                            color = Color.White,
                            text = "광고 선택",
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
