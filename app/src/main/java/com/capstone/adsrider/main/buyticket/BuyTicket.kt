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
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Column {
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .width(300.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(MaterialTheme.colors.secondary)
            ) {
                Text(
                    text = "1년권",
                    fontSize = 50.sp,
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "결제하기")
            }
        }
    }
}

@Composable
fun MonthTicketScreen() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Column {
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .width(300.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(MaterialTheme.colors.secondary)
            ) {
                Text(
                    text = "1달권",
                    fontSize = 50.sp,
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "결제하기")
            }
        }
    }
}

@Composable
fun DayTicketScreen() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Column {
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .width(300.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(MaterialTheme.colors.secondary)
            ) {
                Text(
                    text = "1일권",
                    fontSize = 50.sp,
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "결제하기")
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
