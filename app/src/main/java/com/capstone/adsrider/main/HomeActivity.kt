package com.capstone.adsrider.main

import android.Manifest
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.capstone.adsrider.R
import com.capstone.adsrider.account.AccountScreen
import com.capstone.adsrider.intro.LoginActivity
import com.capstone.adsrider.intro.LoginViewModel
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
fun MyPage(accountViewModel: AccountViewModel = viewModel(), loginViewModel: LoginViewModel = viewModel()) {
    val balance = accountViewModel.balance.collectAsState().value
    val user = loginViewModel.user.collectAsState().value
    var loginState by remember { mutableStateOf(true) }
    val context = LocalContext.current

    if (!loginState) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
        context.startActivity(intent)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        accountViewModel.getBalance()
        loginViewModel.getUserInfo()
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                style = MaterialTheme.typography.h4,
                color = colorResource(R.color.dark_blue),
                text = "마이 라이딩"
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                style = MaterialTheme.typography.h5,
                text = "${user.email}님")
            if (user.expire_date == 0L) {
                Canvas(
                    modifier = Modifier
                        .width(300.dp)
                        .height(150.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    drawRoundRect(
                        size = size,
                        color = Color.Black,
                        cornerRadius = CornerRadius(30f, 30f),
                        style = Stroke(
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
                        )
                    )
                    drawIntoCanvas { canvas ->
                        val text = "이용권 없음"
                        val paint = Paint().apply {
                            color = Color.Black.toArgb()
                            textSize = 24.sp.toPx()
                            textAlign = Paint.Align.CENTER
                        }
                        val x = size.width / 2f
                        val y = size.height / 2f + (paint.fontMetrics.descent - paint.fontMetrics.ascent) / 2f
                        canvas.nativeCanvas.drawText(text, x, y, paint)
                    }
                }
            }
            else {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(150.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorResource(id = R.color.light_blue))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(painter = painterResource(id = R.drawable.adsrider_logo), contentDescription = "logo")
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 10.dp, bottom = 10.dp),
                            text = user.expire_date.toString()
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(vertical = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Color.LightGray),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //총 라이딩 횟수
                    Text(text = "라이딩")
                    Text(text = "0")
                }
                Divider(modifier = Modifier
                    .height(50.dp)
                    .width(1.dp)
                )
                Column(
                    modifier = Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "KRW")
                    balance.forEach{
                        if (it.type == "KRW") {
                            Text(text = it.amount)
                        }
                    }
                }
                Divider(modifier = Modifier
                    .height(50.dp)
                    .width(1.dp)
                )
                Column(
                    modifier = Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "ADS")
                    balance.forEach{
                        if (it.type == "ADS") {
                            Text(text = it.amount)
                        }
                    }
                }
            }
            Text(
                modifier = Modifier
                    .clickable {
                        accountViewModel.stopAccount()
                        loginViewModel.logout()
                        loginState = false
                    },
                style = TextStyle(textDecoration = TextDecoration.Underline),
                color = Color.Gray,
                fontSize = 15.sp,
                text = "로그아웃"
            )
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
