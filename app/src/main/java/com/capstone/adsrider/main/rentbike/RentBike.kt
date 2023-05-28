package com.capstone.adsrider.main.rentbike

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.SystemClock.sleep
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capstone.adsrider.R
import com.capstone.adsrider.intro.LoginViewModel
import com.capstone.adsrider.main.HomeActivity
import com.capstone.adsrider.utility.QRcodeAnalyser
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentBikeScreen() {
    val navController = rememberNavController()

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
            AdsExposure(imageId!!, navController = navController)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRScanner(navController: NavHostController, loginViewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val user = loginViewModel.user.collectAsState().value
    var preview by remember { mutableStateOf<Preview?>(null) }
    val barCodeVal = remember { mutableStateOf("") }
    var state by remember { mutableStateOf(false) }


    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    val locationPermissionState2 = rememberPermissionState(permission = Manifest.permission.ACCESS_COARSE_LOCATION)

    loginViewModel.getUserInfo()

    if (user.expired_date <= System.currentTimeMillis()) {
        Dialog(onDismissRequest = {}) {
            Surface(
                modifier = Modifier
                    .height(500.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Column {
                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        "이용권 필요",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .padding(vertical = 8.dp),
                        fontSize = 20.sp,
                        lineHeight = 17.sp
                    )
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        "이용권 구매 시 자전거 대여 가능",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .padding(vertical = 8.dp),
                        fontSize = 13.sp
                    )
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                            .fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            val intent = Intent(context, HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(R.color.dark_blue)
                        )
                    ) {
                        Text("확인", fontSize = 16.sp, color = Color.White)
                    }
                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }

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
                Button(
                    onClick = { navController.navigate("path find") },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.dark_blue)
                    )
                ) {
                    Text("대여", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { state = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.dark_blue)
                    )) {
                    Text("취소", color = Color.White)
                }
            }
        )
    }
}
