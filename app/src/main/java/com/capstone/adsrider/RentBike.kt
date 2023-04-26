package com.capstone.adsrider

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.capstone.adsrider.ui.theme.AdsRiderTheme

@Composable
fun RentBikeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { navController.navigate("NaverMap") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "QR코드 등록")
        }
    }
}
