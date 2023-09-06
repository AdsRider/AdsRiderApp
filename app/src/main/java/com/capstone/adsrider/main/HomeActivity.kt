package com.capstone.adsrider.main

import android.Manifest
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capstone.adsrider.R
import com.capstone.adsrider.intro.LoginActivity
import com.capstone.adsrider.intro.LoginViewModel
import com.capstone.adsrider.main.account.AccountScreen
import com.capstone.adsrider.main.account.AccountViewModel
import com.capstone.adsrider.main.buyticket.BuyTicketScreen
import com.capstone.adsrider.main.rentbike.AdsExposure
import com.capstone.adsrider.main.rentbike.AdsView
import com.capstone.adsrider.main.rentbike.PathFindScreen
import com.capstone.adsrider.main.rentbike.QRScanner
import com.capstone.adsrider.main.swapcoin.SwapCoinScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.endAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.overlayingComponent
import com.patrykandpatrick.vico.compose.component.shape.roundedCornerShape
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.legend.verticalLegend
import com.patrykandpatrick.vico.compose.legend.verticalLegendItem
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.chart.segment.SegmentProperties
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.core.marker.Marker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
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

fun convertTimestampToDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy년 MM월 dd일까지", Locale.getDefault())
    return sdf.format(timestamp).toString()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdsRider() {
    val navController = rememberNavController()
    var showBottomBar by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var currentRoute = navBackStackEntry?.destination?.route
    Log.d("currentRoute", currentRoute.toString())

    showBottomBar = currentRoute?.contains("ad exposure") == false

    Scaffold(
        topBar = {
            if (showBottomBar) {
                TopBar(navController = navController)
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation(navController = navController)
            }
        },
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
        color = Color.White,
    ) {
        Box(
            modifier = Modifier
                .height(75.dp)
                .padding(horizontal = 16.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.height(50.dp),
                    painter = painterResource(R.drawable.adsrider_name),
                    contentDescription = "",
                )
            }
            IconButton(
                onClick = {
                    navController.navigate("My Page") {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                Icon(
                    painter = painterResource(id = BottomNavItem.MyPage.icon),
                    contentDescription = "My Page",
                    modifier = Modifier.size(40.dp),
                    tint = colorResource(R.color.dark_blue),
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
        contentAlignment = Alignment.Center,
    ) {
        Image(painter = painterResource(id = R.drawable.adsrider_logo), contentDescription = "logo")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyPage(
    accountViewModel: AccountViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel(),
    statisticViewModel: StatisticViewModel = viewModel(),
) {
    val balance = accountViewModel.balance.collectAsState().value
    val user = loginViewModel.user.collectAsState().value
    val statistic = statisticViewModel.statistic.collectAsState().value
    val logoutState = loginViewModel.logoutState.collectAsState().value
    val context = LocalContext.current
    val meters = Array(7) { 0 }
    val reward = Array(7) { 0f }

    if (logoutState == "success") {
        loginViewModel.setSignInState("")
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
        context.startActivity(intent)
    } else if (logoutState != "") {
        Toast.makeText(context, logoutState, Toast.LENGTH_SHORT).show()
        loginViewModel.setSignInState("")
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        accountViewModel.getBalance()
        loginViewModel.getUserInfo()
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                style = MaterialTheme.typography.h4,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.dark_blue),
                text = "마이라이딩",
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                style = MaterialTheme.typography.h5,
                text = " ${user.email}님",
            )
            if (user.expired_date <= System.currentTimeMillis()) {
                Canvas(
                    modifier = Modifier
                        .width(300.dp)
                        .height(180.dp)
                        .align(Alignment.CenterHorizontally),
                ) {
                    drawRoundRect(
                        size = size,
                        color = Color.Black,
                        cornerRadius = CornerRadius(30f, 30f),
                        style = Stroke(
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f),
                        ),
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
            } else {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(180.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorResource(id = R.color.light_blue)),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(painter = painterResource(id = R.drawable.adsrider_logo), contentDescription = "logo")
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End,
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 10.dp, bottom = 10.dp),
                            text = convertTimestampToDate(user.expired_date),
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "ADS", fontSize = 15.sp)
                }
                Column(
                    modifier = Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    balance.forEach {
                        if (it.type == "ADS") {
                            Text(text = it.amount, fontSize = 15.sp)
                        }
                    }
                }
            }

            var today: String

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = Date()
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            val toDate = sdf.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, -6)
            val fromDate = sdf.format(calendar.time)
            statisticViewModel.getStatistic(fromDate, toDate)

            var i = 0
            statistic.forEach {
                meters[i] = it.meters
                reward[i] = it.reward.toFloat()
                i++
            }

            val chartEntryModelProducer1 = ChartEntryModelProducer(
                entriesOf(
                    meters[0],
                    meters[1],
                    meters[2],
                    meters[3],
                    meters[4],
                    meters[5],
                    meters[6],
                ),
            )
            val chartEntryModelProducer2 = ChartEntryModelProducer(
                entriesOf(
                    reward[0],
                    reward[1],
                    reward[2],
                    reward[3],
                    reward[4],
                    reward[5],
                    reward[6],
                ),
            )

            val bottomLabel = mutableListOf<String>()

            for (i in 0..6) {
                today = SimpleDateFormat("MM/dd", Locale.getDefault()).format(calendar.time)
                bottomLabel += today
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            val defaultLines = currentChartStyle.lineChart.lines
            val defaultColumns = currentChartStyle.columnChart.columns

            val composedChartEntryModelProducer = chartEntryModelProducer1 + chartEntryModelProducer2

            val columnChart = columnChart(
                remember(defaultColumns) {
                    defaultColumns.map { defaultColumn ->
                        LineComponent(
                            color = android.graphics.Color.RED,
                            defaultColumn.thicknessDp,
                            Shapes.roundedCornerShape(2.dp),
                        )
                    }
                },
                targetVerticalAxisPosition = AxisPosition.Vertical.Start,
            )
            val lineChart = lineChart(
                remember(defaultLines) {
                    defaultLines.map { defaultLine ->
                        defaultLine.copy(
                            pointConnector = DefaultPointConnector(cubicStrength = 0f),
                            lineColor = android.graphics.Color.BLUE,
                            lineBackgroundShader = null,
                        )
                    }
                },
                targetVerticalAxisPosition = AxisPosition.Vertical.End,
            )

            Chart(
                modifier = Modifier.height(200.dp),
                chart = remember(columnChart, lineChart) { columnChart + lineChart },
                chartModelProducer = composedChartEntryModelProducer,
                startAxis = startAxis(),
                endAxis = endAxis(),
                bottomAxis = bottomAxis(
                    valueFormatter = { x, _ -> bottomLabel[x.toInt() % bottomLabel.size] },
                ),
                marker = rememberMarker(),
                legend = rememberLegend(),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier
                    .clickable {
                        accountViewModel.stopAccount()
                        loginViewModel.logout()
                    },
                style = TextStyle(textDecoration = TextDecoration.Underline),
                color = Color.Gray,
                fontSize = 15.sp,
                text = "로그아웃",
            )
        }
    }
}

@Composable
fun rememberMarker(): Marker {
    val labelBackgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
    val labelBackground = remember(labelBackgroundColor) {
        ShapeComponent(MarkerCorneredShape(Corner.FullyRounded), labelBackgroundColor.toArgb()).setShadow(
            radius = 4f,
            dy = 2f,
            applyElevationOverlay = true,
        )
    }
    val label = textComponent(
        background = labelBackground,
        lineCount = 1,
        padding = dimensionsOf(8.dp, 4.dp),
        typeface = Typeface.MONOSPACE,
    )
    val indicatorInnerComponent = shapeComponent(
        Shapes.pillShape,
        androidx.compose.material3.MaterialTheme.colorScheme.surface,
    )
    val indicatorCenterComponent = shapeComponent(Shapes.pillShape, Color.White)
    val indicatorOuterComponent = shapeComponent(Shapes.pillShape, Color.White)
    val indicator = overlayingComponent(
        outer = indicatorOuterComponent,
        inner = overlayingComponent(
            outer = indicatorCenterComponent,
            inner = indicatorInnerComponent,
            innerPaddingAll = 5.dp,
        ),
        innerPaddingAll = 10.dp,
    )
    val guideline = lineComponent(
        androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(.2f),
        2.dp,
        DashedShape(Shapes.pillShape, 8f, 4f),
    )
    return remember(label, indicator, guideline) {
        object : MarkerComponent(label, indicator, guideline) {
            init {
                indicatorSizeDp = 36f
                onApplyEntryColor = { entryColor ->
                    indicatorOuterComponent.color = entryColor.copyColor(32)
                    with(indicatorCenterComponent) {
                        color = entryColor
                        setShadow(radius = 12f, color = entryColor)
                    }
                }
            }

            override fun getInsets(context: MeasureContext, outInsets: Insets, segmentProperties: SegmentProperties) =
                with(context) {
                    outInsets.top = label.getHeight(context) +
                        MarkerCorneredShape(Corner.FullyRounded).tickSizeDp.pixels +
                        4f.pixels * 1.3f - 2f.pixels
                }
        }
    }
}

val chartColor = listOf(Color.Blue, Color.Red)
val chartLegend = listOf("리워드 지급", "주행거리")

@Composable
private fun rememberLegend() = verticalLegend(
    items = chartColor.mapIndexed { index, chartColor ->
        verticalLegendItem(
            icon = shapeComponent(Shapes.pillShape, chartColor),
            label = textComponent(
                color = currentChartStyle.axis.axisLabelColor,
                textSize = 12.sp,
                typeface = Typeface.MONOSPACE,
            ),
            labelText = chartLegend[index],
        )
    },
    iconSize = 8.dp,
    iconPadding = 10.dp,
    spacing = 4.dp,
    padding = dimensionsOf(top = 8.dp),
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.screenRoute,
    ) {
        composable(BottomNavItem.Home.screenRoute) { HomeScreen() }
        composable(BottomNavItem.RentBike.screenRoute) { QRScanner(navController = navController) }
        composable(BottomNavItem.ButTicket.screenRoute) { BuyTicketScreen() }
        composable(BottomNavItem.Withdrawal.screenRoute) { AccountScreen() }
        composable(BottomNavItem.SwapCoin.screenRoute) { SwapCoinScreen() }
        composable(BottomNavItem.MyPage.screenRoute) { MyPage() }

        // rentbike
        composable("path find") {
            PathFindScreen(navController = navController)
        }
        composable("rent bike") {
            QRScanner(navController = navController)
        }
        composable("ad select") {
            AdsView(navController = navController)
        }
        composable(
            "ad exposure/{imageId}",
            arguments = listOf(
                navArgument("imageId") {
                    type = NavType.IntType
                },
            ),
        ) {
            val imageId = it.arguments?.getInt("imageId")
            AdsExposure(imageId!!, navController = navController)
        }
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
        BottomNavItem.Withdrawal,
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.screenRoute,
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
                },
            )
        }
    }
}
