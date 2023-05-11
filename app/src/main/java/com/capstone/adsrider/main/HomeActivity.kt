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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.capstone.adsrider.account.AccountScreen
import com.capstone.adsrider.main.account.AccountViewModel
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
    object MyPage : BottomNavItem(R.drawable.user_info, "My Page")
}

@Composable
fun AdsRider() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBar(navController = navController) },
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
fun TopBar(navController: NavController) {
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
                onClick = { navController.navigate("My Page") },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = BottomNavItem.MyPage.icon),
                    contentDescription = "My Page",
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
            .background(colorResource(id = R.color.light_blue)),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = R.drawable.adsrider_logo), contentDescription = "logo")
    }
}

@Composable
fun MyPage(accountViewModel: AccountViewModel = viewModel()) {
    val balance = accountViewModel.balance.collectAsState().value
    Surface(
        modifier = Modifier.fillMaxSize()
        ) {
        accountViewModel.getBalance()
        Column() {
            balance.forEach {
                Text(text = "잔액 : ${it.amount} ${it.type}\n")
            }
        }

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
        composable(BottomNavItem.Withdrawal.screenRoute) { AccountScreen() }
        composable(BottomNavItem.SwapCoin.screenRoute) { SwapCoinScreen() }
        composable(BottomNavItem.MyPage.screenRoute) { MyPage() }
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

@Preview(showBackground = true)
@Composable
fun preview() {
    MyPage()
}
