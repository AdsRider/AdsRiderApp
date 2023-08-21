package com.capstone.adsrider.main.buyticket

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.adsrider.R

@Composable
fun BuyTicketScreen(buyTicketViewModel: BuyTicketViewModel = viewModel()) {
    val state = buyTicketViewModel.state.collectAsState().value
    val context = LocalContext.current
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    val stateArray = remember { mutableStateListOf(false, false, false) }

    if (state == "success") {
        Toast.makeText(context, "구매 완료", Toast.LENGTH_SHORT).show()
        buyTicketViewModel.setState("")
    } else if (state == "fail") {
        Toast.makeText(context, "결제에 실패하였습니다", Toast.LENGTH_SHORT).show()
        buyTicketViewModel.setState("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "이용권 구매",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 15.dp, vertical = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorResource(R.color.light_blue))
                .clickable { stateArray[0] = true }
        ) {
            Column(
                Modifier.align(Alignment.Center).padding(20.dp)
            ) {
                Text(
                    text = "1일권",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "1일 이용 기한 추가",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "결제 금액   2,000 ADS",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
        Canvas(Modifier.fillMaxWidth().height(1.dp).padding(horizontal = 30.dp)) {
            drawLine(
                color = Color.Gray,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 5.0f,
                pathEffect = pathEffect
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 15.dp, vertical = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorResource(R.color.light_blue))
                .clickable { stateArray[1] = true }
        ) {
            Column(
                Modifier.align(Alignment.Center).padding(20.dp),
            ) {
                Text(
                    text = "1달권",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "30일 이용 기한 추가",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "결제 금액   7,000 ADS",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
        Canvas(Modifier.fillMaxWidth().height(1.dp).padding(horizontal = 30.dp)) {
            drawLine(
                color = Color.Gray,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 5.0f,
                pathEffect = pathEffect
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 15.dp, vertical = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorResource(R.color.light_blue))
                .clickable { stateArray[2] = true }
        ) {
            Column(
                Modifier.align(Alignment.Center).padding(20.dp),
            ) {
                Text(
                    text = "1년권",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "365일 이용 기한 추가",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "결제 금액   30,000 ADS",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
    if (stateArray[0]) {
        AlertDialog(
            onDismissRequest = { stateArray[0] = false },
            title = {
                Text("1일권 구매")
            },
            text = {
                Text("1일권을 구매하시겠습니까?")
            },
            confirmButton = {
                Button(
                    onClick = { buyTicketViewModel.buyTicket(1) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.dark_blue)
                    )
                ) {
                    Text("구매", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { stateArray[0] = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.light_blue)
                    )) {
                    Text("취소", color = Color.White)
                }
            }
        )
    }
    if (stateArray[1]) {
        AlertDialog(
            onDismissRequest = { stateArray[1] = false },
            title = {
                Text("1달권 구매")
            },
            text = {
                Text("1달권을 구매하시겠습니까?")
            },
            confirmButton = {
                Button(
                    onClick = { buyTicketViewModel.buyTicket(30) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.dark_blue)
                    )
                ) {
                    Text("구매", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { stateArray[1] = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.light_blue)
                    )) {
                    Text("취소", color = Color.White)
                }
            }
        )
    }
    if (stateArray[2]) {
        AlertDialog(
            onDismissRequest = { stateArray[2] = false },
            title = {
                Text("1년권 구매")
            },
            text = {
                Text("1년권을 구매하시겠습니까?")
            },
            confirmButton = {
                Button(
                    onClick = { buyTicketViewModel.buyTicket(365) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.dark_blue)
                    )
                ) {
                    Text("구매", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { stateArray[2] = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.light_blue)
                    )) {
                    Text("취소", color = Color.White)
                }
            }
        )
    }
}
