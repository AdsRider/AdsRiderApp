package com.capstone.adsrider.main.buyticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.adsrider.TicketTabItem
import com.capstone.adsrider.ui.theme.AdsRiderTheme


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
    Box(Modifier.fillMaxSize(), Alignment.Center) {
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
    }
}

@Composable
fun MonthTicketScreen() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
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
    }
}

@Composable
fun DayTicketScreen() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
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

@Preview(showBackground = true)
@Composable
fun preview() {
    AdsRiderTheme {
        YearTicketScreen()
    }
}
