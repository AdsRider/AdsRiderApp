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
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.adsrider.R

@Composable
fun SwapCoinScreen(viewModel: SwapCoinViewModel = viewModel()) {
    var amount by remember { mutableStateOf("") }
    val state = viewModel.state.collectAsState().value
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
            .padding(40.dp),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "결제방법",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(20.dp))
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
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "구매 가격",
            modifier = Modifier.padding(5.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            modifier = Modifier.height(50.dp),
            value = amount,
            onValueChange = { amount = it },
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            viewModel.purchaseCoin(amount)
        }) {
            Text(
                text = "구매하기",
                fontSize = 20.sp,
            )
        }
    }
}
