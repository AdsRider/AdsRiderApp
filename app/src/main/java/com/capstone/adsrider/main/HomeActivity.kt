package com.capstone.adsrider

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.capstone.adsrider.main.buyticket.BuyTicketScreen
import com.capstone.adsrider.main.rentbike.RentBikeScreen
import com.capstone.adsrider.main.swapcoin.SwapCoinScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                AdsRider()
            }
        }
    }
}

sealed class BottomNavItem(val icon: Int, val screenRoute: String) {
    object Home : BottomNavItem(R.drawable.home, "HOME")
    object RentBike : BottomNavItem(R.drawable.rent_bike, "RENT BIKE")
    object SwapCoin : BottomNavItem(R.drawable.swap_coin, "SWAP COIN")
    object ButTicket : BottomNavItem(R.drawable.buy_ticket, "BUY TICKET")
    object Withdrawal : BottomNavItem(R.drawable.withdrawal, "WITHDRAWAL")
}
sealed class TicketTabItem(val term: String, val screenRoute: String) {
    object Day : TicketTabItem("1일권", "DAY")
    object Month : TicketTabItem("1달권", "MONTH")
    object Year : TicketTabItem("1년권", "YEAR")
}

@Composable
fun AdsRider() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBar() },
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) {
        Box(Modifier.padding(it)) {
            BottomNavigationGraph(navController = navController)
        }
    }
}

@Composable
fun TopBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .height(75.dp)
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.height(50.dp),
                    painter = painterResource(R.drawable.adsrider_name),
                    contentDescription = "")
            }
            IconButton(
                onClick = {  },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.user_info),
                    contentDescription = "USER INFO",
                    modifier = Modifier.size(40.dp),
                    tint = colorResource(R.color.dark_blue)
                )
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(600.dp)
            .background(MaterialTheme.colors.onSurface)
    ) {
        Text(
            text = "메인 페이지",
            fontSize = 50.sp,
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun BottomNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.screenRoute
    ) {
        composable(BottomNavItem.Home.screenRoute) { HomeScreen() }
        composable(BottomNavItem.RentBike.screenRoute) { RentBikeScreen() }
        composable(BottomNavItem.ButTicket.screenRoute) { BuyTicketScreen() }
        composable(BottomNavItem.Withdrawal.screenRoute) { WithdrawalScreen() }
        composable(BottomNavItem.SwapCoin.screenRoute) { SwapCoinScreen() }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.RentBike,
        BottomNavItem.ButTicket,
        BottomNavItem.SwapCoin,
        BottomNavItem.Withdrawal
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.screenRoute
                    )
                },
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        if (item == BottomNavItem.RentBike) {
                            cameraPermissionState.launchPermissionRequest()
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
