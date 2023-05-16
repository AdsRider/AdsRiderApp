package com.capstone.adsrider.main.rentbike

import android.Manifest
import android.os.Bundle
import android.os.SystemClock.sleep
import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capstone.adsrider.main.pathfind.PathFindScreen
import com.capstone.adsrider.utility.QRcodeAnalyser
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalPermissionsApi
class RentBike : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentBikeScreen()
        }
    }
}

@Composable
fun RentBikeScreen() {
    val navController = rememberNavController()
    Scaffold {
        Box(Modifier.padding(it)) {
            RentBikeNavigationGraph(navController = navController)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RentBikeNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "rent bike"
    ) {
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
                }
            )
        ) {
            val imageId = it.arguments?.getInt("imageId")
            AdsExposure(imageId!!)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRScanner(navController: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    val barCodeVal = remember { mutableStateOf("") }
    var state by remember { mutableStateOf(false) }

    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    val locationPermissionState2 = rememberPermissionState(permission = Manifest.permission.ACCESS_COARSE_LOCATION)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { AndroidViewContext ->
                PreviewView(AndroidViewContext).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                val cameraSelector: CameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
                val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                    ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({
                    preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                    val barcodeAnalyser = QRcodeAnalyser { barcodes ->
                        barcodes.forEach { barcode ->
                            barcode.rawValue?.let { barcodeValue ->
                                barCodeVal.value = barcodeValue
                                if (barcodeValue == "https://adsrider.wo.tc/") {
                                    sleep(100)
                                    state = true
                                }
                            }
                        }
                    }
                    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                        }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.5F),
                    shape = RoundedCornerShape(10.dp)
                ),
            text = "대여할 자전거의 QR을 찍으세요",
            fontSize = 25.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Button(onClick = {
            navController.navigate("path find") {
                if (!locationPermissionState.status.isGranted) {
                    locationPermissionState.launchPermissionRequest()
                    locationPermissionState2.launchPermissionRequest()
                }
            }
        }) {
            Text("대여")
        }
    }
    if (state) {
        AlertDialog(
            onDismissRequest = { state = false },
            title = {
                Text("자전거 대여")
            },
            text = {
                Text("이 자전거를 대여하시겠습니까?")
            },
            confirmButton = {
                Button(onClick = { navController.navigate("path find") }) {
                    Text("대여")
                }
            },
            dismissButton = {
                Button(onClick = { state = false }) {
                    Text("취소")
                }
            }
        )
    }
}
