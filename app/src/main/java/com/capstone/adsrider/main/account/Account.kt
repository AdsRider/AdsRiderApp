package com.capstone.adsrider.main.account

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.adsrider.utility.App
import com.capstone.adsrider.utility.UserSharedPreference
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun AccountScreen(accountViewModel: AccountViewModel = viewModel()) {
    val navController = rememberNavController()
    val balance = accountViewModel.balance.collectAsState().value
    val history = accountViewModel.history.collectAsState().value

    Log.d("balance", balance.toString())
    Log.d("history", history.toString())

    @Composable
    fun DepositScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "입금주소",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(30.dp))
            Card(
                elevation = 4.dp,
                shape = RoundedCornerShape(size = 12.dp)
            ) {
                Text(
                    text = UserSharedPreference(App.context()).getUserPrefs("address"),
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "※주의사항※\n" +
                    "반드시 입금 주소를 확인 후 전송하시기 바랍니다.\n" +
                    "주소 혼동으로 인한 손실에 대해서는 책임 지지 않습니다.",
                color = Color.Gray
            )
        }
    }

    @Composable
    fun WithdrawalScreen() {
        var address by remember { mutableStateOf("") }
        var count by remember { mutableStateOf("") }
        var type by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "출금 주소",
                modifier = Modifier.padding(5.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    modifier = Modifier.height(50.dp),
                    value = address,
                    onValueChange = { address = it }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "출금 수량",
                modifier = Modifier.padding(5.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold

            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    modifier = Modifier.height(50.dp),
                    value = count,
                    onValueChange = { count = it }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "출금 유형",
                modifier = Modifier.padding(5.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = type == "ADS",
                    onClick = {
                        type = "ADS"
                    }
                )
                Text("ADS")
                RadioButton(
                    selected = type == "KRW",
                    onClick = {
                        type = "KRW"
                    }
                )
                Text("KRW")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                accountViewModel.withdrawal(address, count)
            }) {
                Text(
                    text = "출금하기",
                    fontSize = 20.sp
                )
            }
        }
    }

    @Composable
    fun HistoryScreen() {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(history) {
                val sdf = SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
                val netDate = Date(it.timestamp)
                val date = sdf.format(netDate)

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)) {
                    Text(text = "${date}\n")
                    Text(text = it.type, style = MaterialTheme.typography.h5)
                    if (it.type == "이용권구매") {
                        Text(
                            text = "출금 ${it.amount} ADS",
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.End)
                        )
                    }
                    else {
                        Text(
                            text = "입금 ${it.amount} ADS",
                            color = Color.Blue,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.End)
                        )
                    }
                    Divider(Modifier.fillMaxWidth().padding(top = 16.dp), thickness = 3.dp)
                }
            }
        }
    }

    @Composable
    fun AccountNavigationGraph(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = AccountTabItem.Deposit.screenRoute
        ) {
            composable(AccountTabItem.Deposit.screenRoute) {
                DepositScreen()
            }
            composable(AccountTabItem.Withdrawal.screenRoute) {
                WithdrawalScreen()
            }
            composable(AccountTabItem.History.screenRoute) {
                HistoryScreen()
            }
        }
    }

    @Composable
    fun AccountNavigation(navController: NavController) {
        var state by remember { mutableStateOf(0) }
        val items = listOf(
            AccountTabItem.Deposit,
            AccountTabItem.Withdrawal,
            AccountTabItem.History
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
                        if (item.screenRoute == "HISTORY") {
                            accountViewModel.getBalance()
                            accountViewModel.getHistory()
                        }
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

    Scaffold(
        topBar = { AccountNavigation(navController = navController) }
    ) {
        Box(Modifier.padding(it)) {
            AccountNavigationGraph(navController = navController)
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}

sealed class AccountTabItem(val term: String, val screenRoute: String) {
    object Deposit : AccountTabItem("입금", "DEPOSIT")
    object Withdrawal : AccountTabItem("출금", "WITHDRAWAL")
    object History : AccountTabItem("내역", "HISTORY")
}
