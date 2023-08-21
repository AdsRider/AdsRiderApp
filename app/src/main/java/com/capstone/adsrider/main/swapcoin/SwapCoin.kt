package com.capstone.adsrider.main.swapcoin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.adsrider.R
import com.capstone.adsrider.intro.LoginViewModel
import com.capstone.adsrider.main.account.AccountViewModel

@Composable
fun SwapCoinScreen(
    viewModel: SwapCoinViewModel = viewModel(),
    accountViewModel: AccountViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    var amount by remember { mutableStateOf(0.0) }
    val state = viewModel.state.collectAsState().value
    val balance = accountViewModel.balance.collectAsState().value
    val context = LocalContext.current

    if (state == "success") {
        Toast.makeText(context, "구매 완료", Toast.LENGTH_SHORT).show()
        viewModel.setState("")
    } else if (state == "fail") {
        Toast.makeText(context, "결제에 실패하였습니다", Toast.LENGTH_SHORT).show()
        viewModel.setState("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Column(modifier = Modifier.border(2.dp, Color.LightGray).padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "결제수단 선택",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.LightGray)
                        .height(40.dp)
                        .padding(10.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tosspay),
                        contentDescription = "tosspay",
                    )
                }
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.LightGray)
                        .height(40.dp)
                        .padding(10.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.kakaopay),
                        contentDescription = "kakaopay",
                    )
                }
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.LightGray)
                        .height(40.dp)
                        .padding(10.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.payco),
                        contentDescription = "payco",
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "구매 가격",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(10.dp))

        val items = listOf(1000, 3000, 5000, 10000, 30000, 50000, 100000)
        val selectedValue = remember { mutableStateOf(items[0]) }
        val isSelectedItem: (Int) -> Boolean = { selectedValue.value == it }
        val onChangeState: (Int) -> Unit = { selectedValue.value = it }
        accountViewModel.getBalance()
        loginViewModel.getUserInfo()

        Column(Modifier.padding(8.dp)) {
            items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .selectable(
                            selected = isSelectedItem(item),
                            onClick = { onChangeState(item) },
                            role = Role.RadioButton
                        )
                        .padding(8.dp)
                ) {
                    RadioButton(
                        selected = isSelectedItem(item),
                        onClick = null
                    )
                    Text(
                        text = "${item}ADS",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            balance.forEach{
                if (it.type == "ADS") {
                    amount = it.amount.toDouble()
                }
            }
            Divider(Modifier.fillMaxWidth().padding(vertical = 10.dp), thickness = 3.dp)
            Text(
                text = "충전 후 보유 코인 : ${selectedValue.value + amount}ADS",
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                modifier = Modifier.width(280.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(R.color.dark_blue),
                ),
                onClick = {
                    viewModel.purchaseCoin(selectedValue.value.toString())
                },
            ) {
                Text(
                    text = "구매하기",
                    fontSize = 20.sp,
                    color = Color.White,
                )
            }
        }
    }
}
