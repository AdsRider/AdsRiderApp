package com.capstone.adsrider.intro

import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.adsrider.AdsRider
import com.capstone.adsrider.R

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                LoginGraph()
            }
        }
    }
}

sealed class Login(val router: String) {
    object UserLogin : Login("LOGIN")
    object SignIn : Login("SIGNIN")
    object Home : Login("HOME")
}

@Composable
fun LoginGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login.UserLogin.router) {
        composable(Login.UserLogin.router) {
            LoginView(navController = navController)
        }
        composable(Login.SignIn.router) {
            SignInView(navController = navController)
        }
        composable(Login.Home.router) {
            AdsRider()
        }
    }
}

@Composable
fun LoginView(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var loginState = loginViewModel.loginState.collectAsState().value
    var email by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (loginState == "success") {
        loginViewModel.setLoginState("")
        navController.navigate("HOME")
    } else if (loginState != "") {
        Toast.makeText(context, loginState, Toast.LENGTH_SHORT).show()
        loginViewModel.setLoginState("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.adsrider_logo),
            contentDescription = "App Icon"
        )
        OutlinedTextField(
            value = email,
            singleLine = true,
            placeholder = { Text(text = "이메일", color = Color.Gray) },
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.user_info), contentDescription = "email")
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = passwd,
            singleLine = true,
            placeholder = { Text(text = "비밀번호", color = Color.Gray) },
            onValueChange = { passwd = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.password), contentDescription = "passwd")
            },
            trailingIcon = {
                val icon = if (passwordVisible) R.drawable.visibility_off else R.drawable.visibility_on
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton( onClick = {passwordVisible = !passwordVisible} ) {
                    Icon(painter = painterResource(id = icon), contentDescription = description)
                }
            }
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(R.color.dark_blue)
            ),
            onClick = {
                loginViewModel.login(email, passwd)
            }
        ) {
            Text(
                color = Color.White,
                text = "로그인"
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .clickable { navController.navigate("SIGNIN") },
            style = TextStyle(textDecoration = TextDecoration.Underline),
            color = Color.Gray,
            fontSize = 15.sp,
            text = "회원이 아니신가요? 회원가입"
        )
    }
}

@Composable
fun SignInView(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var signInState = loginViewModel.signInState.collectAsState().value
    var email by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (signInState == "success") {
        loginViewModel.setSignInState("")
        navController.navigate("HOME")
    } else if (signInState != "") {
        Toast.makeText(context, signInState, Toast.LENGTH_SHORT).show()
        loginViewModel.setSignInState("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.h4,
            color = colorResource(id = R.color.dark_blue),
            text = "회원가입"
        )
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = email,
            singleLine = true,
            placeholder = { Text(text = "이메일", color = Color.Gray) },
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.user_info), contentDescription = "email")
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = passwd,
            singleLine = true,
            placeholder = { Text(text = "비밀번호", color = Color.Gray) },
            onValueChange = { passwd = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.password), contentDescription = "passwd")
            },
            trailingIcon = {
                val icon = if (passwordVisible) R.drawable.visibility_off else R.drawable.visibility_on
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton( onClick = {passwordVisible = !passwordVisible} ) {
                    Icon(painter = painterResource(id = icon), contentDescription = description)
                }
            }
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(R.color.dark_blue),
                contentColor = Color.White
            ),
            onClick = {
                try {
                    if (!isEmpty(email) && !isEmpty(passwd)) {
                        loginViewModel.signin(email, passwd)
                        navController.navigate("LIGIN")
                    } else {
                        Toast.makeText(context, "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(
                color = Color.White,
                text = "등록하기"
            )
        }
    }
}
