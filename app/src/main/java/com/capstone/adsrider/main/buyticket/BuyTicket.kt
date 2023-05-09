package com.capstone.adsrider.main.buyticket

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.adsrider.TicketTabItem


@Composable
fun BuyTicketScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TicketNavigation(navController = navController) }
    ) {
        Box(Modifier.padding(it)) {
            TicketNavigationGraph(navController = navController)
        }
    }
}


@Composable
fun YearTicketScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(400.dp)
                .width(300.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colors.secondary)
        ) {
            Column (
                Modifier.align(Alignment.TopCenter)
            ){
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "1년권",
                    style = MaterialTheme.typography.h2,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "\n1년 동안 이용 가능\n\n무제한 반복 대여 가능\n\n\n결제금액     30,000원",
                    color = Color.White,
                    fontSize = 25.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(
                text = "구매하기",
                fontSize = 25.sp
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(400.dp)
                .width(300.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colors.secondary)
        ) {
            Column (
                Modifier.align(Alignment.TopCenter)
            ){
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "1달권",
                    style = MaterialTheme.typography.h2,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "\n1달 동안 이용 가능\n\n무제한 반복 대여 가능\n\n\n결제금액      7,000원",
                    color = Color.White,
                    fontSize = 25.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(
                text = "구매하기",
                fontSize = 25.sp
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(400.dp)
                .width(300.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colors.secondary)
        ) {
            Column (
                Modifier.align(Alignment.TopCenter)
            ){
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "1일권",
                    style = MaterialTheme.typography.h2,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "\n1일 동안 2시간 이용 가능\n\n대여횟수 제한없이\n재 대여 가능\n\n결제금액      2,000원",
                    color = Color.White,
                    fontSize = 25.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(
                text = "구매하기",
                fontSize = 25.sp
            )
        }
    }
}

@Composable
fun TicketNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = TicketTabItem.Day.screenRoute
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
        TicketTabItem.Year
    )
    TabRow(
        selectedTabIndex = state,
        modifier = Modifier.height(60.dp),
        backgroundColor = Color.White
    ) {
        items.forEachIndexed { index, item ->
            Tab(
                text = {
                    Text(
                        text = item.term,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
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
                }
            )
        }
    }
}
