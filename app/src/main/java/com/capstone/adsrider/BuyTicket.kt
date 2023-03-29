package com.capstone.adsrider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.adsrider.ui.theme.AdsRiderTheme

class BuyTicket : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdsRiderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BuyTicketScreen()
                }
            }
        }
    }
}

sealed class TicketNavItem(val term: String, val screenRoute: String) {
    object Day: TicketNavItem("1일권", "DAY")
    object Month: TicketNavItem("1달권", "MONTH")
    object Year: TicketNavItem("1년권", "YEAR")
}

@Composable
fun TicketNavigationGraph(navController: NavHostController) {
    NavHost(navController = navController,
        startDestination = TicketNavItem.Day.screenRoute) {
        composable(TicketNavItem.Day.screenRoute) {
            DayTicketScreen()
        }
        composable(TicketNavItem.Month.screenRoute) {
            MonthTicketScreen()
        }
        composable(TicketNavItem.Year.screenRoute) {
            YearTicketScreen()
        }
    }
}

@Composable
fun TicketNavigation(navController: NavController) {
    var state by remember { mutableStateOf(0) }
    val items = listOf<TicketNavItem>(
        TicketNavItem.Day,
        TicketNavItem.Month,
        TicketNavItem.Year
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
                            fontWeight = Bold,
                        )
                    },
                selected = state == index,
                onClick = { state = index
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

@Composable
fun BuyTicketScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TicketNavigation(navController = navController)}
    ) {
        Box(Modifier.padding(it)) {
            TicketNavigationGraph(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BuyTicketScreenPreview() {
    AdsRiderTheme {
        BuyTicketScreen()
    }
}