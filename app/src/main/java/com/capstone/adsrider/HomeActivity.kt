package com.capstone.adsrider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.capstone.adsrider.ui.theme.AdsRiderTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdsRiderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AdsRider()
                }
            }
        }
    }
}

sealed class BottomNavItem(val icon: Int, val screenRoute: String) {
    object Home: BottomNavItem(R.drawable.home, "HOME")
    object RentBike: BottomNavItem(R.drawable.rent_bike, "RENT BIKE")
    object SwapCoin: BottomNavItem(R.drawable.swap_coin, "SWAP COIN")
    object ButTicket: BottomNavItem(R.drawable.buy_ticket, "BUY TICKET")
    object Withdrawal: BottomNavItem(R.drawable.withdrawal, "WITHDRAWAL")
}
sealed class TicketTabItem(val term: String, val screenRoute: String) {
    object Day: TicketTabItem("1일권", "DAY")
    object Month: TicketTabItem("1달권", "MONTH")
    object Year: TicketTabItem("1년권", "YEAR")
}

@Composable
fun AdsRider() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigation(navController = navController) }
    ){
        Box(Modifier.padding(it)) {
            BottomNavigationGraph(navController = navController)
        }
    }
}

@Composable
fun TopBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Ads Rider",
                color = Color.Blue,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .background(MaterialTheme.colors.onSurface)
    ){
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
fun SwapCoinScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        Text(
            text = "코인 스왑",
            fontSize = 50.sp,
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
@Composable
fun WithdrawalScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primaryVariant)
    ) {
        Text(
            text = "입출금",
            fontSize = 50.sp,
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
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

@Composable
fun YearTicketScreen() {
    Box(Modifier.fillMaxSize(), Alignment.Center){
        Column {
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .width(300.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(MaterialTheme.colors.secondary),
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
    Box(Modifier.fillMaxSize(), Alignment.Center){
        Column {
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .width(300.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(MaterialTheme.colors.secondary),
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
    Box(Modifier.fillMaxSize(), Alignment.Center){
        Column {
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .width(300.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(MaterialTheme.colors.secondary),
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
    NavHost(navController = navController,
        startDestination = TicketTabItem.Day.screenRoute) {
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
    val items = listOf<TicketTabItem>(
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
                        fontWeight = FontWeight.Bold,
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
fun BottomNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.screenRoute) {
        composable(BottomNavItem.Home.screenRoute) { HomeScreen() }
        composable(BottomNavItem.RentBike.screenRoute) { RentBikeScreen() }
        composable(BottomNavItem.ButTicket.screenRoute) { BuyTicketScreen() }
        composable(BottomNavItem.Withdrawal.screenRoute) { WithdrawalScreen() }
        composable(BottomNavItem.SwapCoin.screenRoute) { SwapCoinScreen() }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf<BottomNavItem>(
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
fun AdsRiderPreview() {
    AdsRiderTheme {
        AdsRider()
    }
}
