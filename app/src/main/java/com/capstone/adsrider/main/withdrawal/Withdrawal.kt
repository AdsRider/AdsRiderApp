package com.capstone.adsrider

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val tableData = listOf(Pair("원화", "x 원"), Pair("ADS", "y 코인"))

@Composable
fun WithdrawalScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        val kinds = listOf("출금", "입금")
        var count by remember { mutableStateOf("") }
        var address by remember { mutableStateOf("") }
        val (selected, setSelected) = remember { mutableStateOf("") }

        Text(text = "입출금", fontSize = 20.sp)
        LazyColumn(Modifier.fillMaxWidth().padding(16.dp)) {
            val column1Weight = .3f // 30%
            val column2Weight = .7f // 70%

            item {
                Row(Modifier.background(Color.Gray)) {
                    TableCell(text = "총 보유 자산", weight = 1f)
                }
            }
            items(tableData) {
                val (id, text) = it
                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = id.toString(), weight = column1Weight)
                    TableCell(text = text, weight = column2Weight)
                }
            }
        }
        KindRadioGroup(
            mItems = kinds,
            selected,
            setSelected
        )

        if (selected == "출금") {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("출금 수량")
                TextField(
                    modifier = Modifier.padding(start = 2.dp),
                    value = count,
                    onValueChange = { count = it }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("출금 주소")
                TextField(
                    modifier = Modifier.padding(start = 2.dp),
                    value = address,
                    onValueChange = { address = it }
                )
            }
            Button(onClick = {
            }) {
                Text(text = "출금")
            }
        } else {
            Text(textAlign = TextAlign.Start, text = "지갑주소: sdiojafoiejoiajifo")
            Text(textAlign = TextAlign.Start, text = "보내는사람: 홍길동")
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

@Composable
fun KindRadioGroup(
    mItems: List<String>,
    selected: String,
    setSelected: (selected: String) -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Row {
            mItems.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected == item,
                        onClick = {
                            setSelected(item)
                        },
                        enabled = true,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Magenta
                        )
                    )
                    Text(text = item, modifier = Modifier.padding(start = 2.dp))
                }
            }
        }
    }
}
