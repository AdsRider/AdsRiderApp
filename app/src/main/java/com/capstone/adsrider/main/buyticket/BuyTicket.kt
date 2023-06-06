package com.capstone.adsrider.main.buyticket

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.adsrider.R

@Composable
fun BuyTicketScreen(buyTicketViewModel: BuyTicketViewModel = viewModel()) {
    val navController = rememberNavController()
    val state = buyTicketViewModel.state.collectAsState().value
    val context = LocalContext.current

    if (state == "success") {
        Toast.makeText(context, "구매 완료", Toast.LENGTH_SHORT).show()
        buyTicketViewModel.setState("")
    } else if (state == "fail") {
        Toast.makeText(context, "결제에 실패하였습니다", Toast.LENGTH_SHORT).show()
        buyTicketViewModel.setState("")
    }

    @Composable
    fun YearTicketScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
                    .height(420.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorResource(R.color.light_blue)),
            ) {
                Column(
                    Modifier.align(Alignment.Center),
                ) {
                    Text(
                        text = "1년권",
                        style = MaterialTheme.typography.h2,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(60.dp))
                    Text(
                        text = "365일 이용 기한 추가",
                        color = Color.White,
                        fontSize = 25.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "결제 금액   30,000 ADS",
                        color = Color.White,
                        fontSize = 25.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier.width(280.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(R.color.dark_blue),
                ),
                onClick = {
                    buyTicketViewModel.buyTicket(365)
                },
            ) {
                Text(
                    text = "구매하기",
                    fontSize = 25.sp,
                    color = Color.White,
                )
            }
        }
    }

    @Composable
    fun MonthTicketScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
                    .height(420.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorResource(R.color.light_blue)),
            ) {
                Column(
                    Modifier.align(Alignment.Center),
                ) {
                    Text(
                        text = "1달권",
                        style = MaterialTheme.typography.h2,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(60.dp))
                    Text(
                        text = "30일 이용 기한 추가",
                        color = Color.White,
                        fontSize = 25.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "결제 금액   7,000 ADS",
                        color = Color.White,
                        fontSize = 25.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier.width(280.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(R.color.dark_blue),
                ),
                onClick = {
                    buyTicketViewModel.buyTicket(30)
                },
            ) {
                Text(
                    text = "구매하기",
                    fontSize = 25.sp,
                    color = Color.White,
                )
            }
        }
    }

    @Composable
    fun DayTicketScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
                    .height(420.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorResource(R.color.light_blue)),
            ) {
                Column(
                    Modifier.align(Alignment.Center),
                ) {
                    Text(
                        text = "1일권",
                        style = MaterialTheme.typography.h2,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(60.dp))
                    Text(
                        text = "1일 이용 기한 추가",
                        color = Color.White,
                        fontSize = 25.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "결제 금액   2,000 ADS",
                        color = Color.White,
                        fontSize = 25.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier.width(280.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(R.color.dark_blue),
                ),
                onClick = {
                    buyTicketViewModel.buyTicket(1)
                },
            ) {
                Text(
                    text = "구매하기",
                    fontSize = 25.sp,
                    color = Color.White,
                )
            }
        }
    }

    @Composable
    fun TicketNavigationGraph(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = TicketTabItem.Day.screenRoute,
        ) {
            composable(TicketTabItem.Day.screenRoute) {
                DayTicketScreen()
            }
            composable(TicketTabItem.Month.screenRoute) {
                MonthTicketScreen()
            }
            composable(TicketTabItem.Year.screenRoute) {
                YearTicketScreen()
            }
        }
    }

    @Composable
    fun TicketNavigation(navController: NavController) {
        var state by remember { mutableStateOf(0) }
        val items = listOf(
            TicketTabItem.Day,
            TicketTabItem.Month,
            TicketTabItem.Year,
        )
        TabRow(
            selectedTabIndex = state,
            modifier = Modifier.height(60.dp),
            backgroundColor = Color.White,
        ) {
            items.forEachIndexed { index, item ->
                Tab(
                    text = {
                        Text(
                            text = item.term,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    selected = state == index,
                    onClick = {
                        state = index
                        navController.navigate(item.screenRoute) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }

    Scaffold(
        topBar = { TicketNavigation(navController = navController) },
    ) {
        Box(Modifier.padding(it)) {
            TicketNavigationGraph(navController = navController)
        }
    }
}

sealed class TicketTabItem(val term: String, val screenRoute: String) {
    object Day : TicketTabItem("1일권", "DAY")
    object Month : TicketTabItem("1달권", "MONTH")
    object Year : TicketTabItem("1년권", "YEAR")
}
