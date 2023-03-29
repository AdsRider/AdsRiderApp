package com.capstone.adsrider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.adsrider.ui.theme.AdsRiderTheme

class YearTicket : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdsRiderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    YearTicketScreen()
                }
            }
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

@Preview(showBackground = true)
@Composable
fun YearTicketScreenPreview() {
    AdsRiderTheme {
        YearTicketScreen()
    }
}