package com.capstone.adsrider.intro

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.service.AdsRiderService
import com.capstone.adsrider.utility.App
import com.capstone.adsrider.utility.UserSharedPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel : ViewModel() {
    private val adsRiderService = AdsRiderService()
    private val _user = MutableStateFlow(UserSharedPreference(App.context()).getUserPrefs())
    val user get() = _user

    private val _loginState = MutableStateFlow("")
    val loginState get() = _loginState

    private val _signInState = MutableStateFlow("")
    val signInState get() = _signInState

    private val _logoutState = MutableStateFlow("")
    val logoutState get() = _logoutState

    fun setLoginState(state: String) {
        _loginState.value = state
    }

    fun setSignInState(state: String) {
        _signInState.value = state
    }

    fun setLogoutState(state: String) {
        _signInState.value = state
    }

    fun login(email: String, passwd: String) {
        viewModelScope.launch {
            try {
                val result = adsRiderService.login(email, passwd)
                UserSharedPreference(App.context()).setUserPrefs(result)
                _loginState.value = "success"
                _user.value = result
            } catch (e: HttpException) {
                val errorMessage = e.message!!
                Log.d("login_error", e.toString())
                if (errorMessage.contains("invalid_id_or_password")) {
                    _loginState.value = "아이디 또는 비밀번호가 잘못되었습니다"
                } else {
                    _loginState.value = "아이디 또는 비밀번호가 잘못되었습니다"
                    // _loginState.value = "알수 없는 에러"
                }
            }
        }
    }
    fun signin(type: String, email: String, passwd: String) {
        viewModelScope.launch {
            try {
                adsRiderService.signIn(type, email, passwd)
                _signInState.value = "success"
            } catch (e: HttpException) {
                Log.d("login_error", e.toString())
                _signInState.value = "회원가입 오류 발생"
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            try {
                adsRiderService.logout()
                _logoutState.value = "success"
            } catch (e: HttpException) {
                Log.d("logout_error", e.toString())
                _logoutState.value = "로그아웃 오류 발생"
            }
        }
    }

    fun getUserInfo() {
        viewModelScope.launch {
            try {
                _user.value = adsRiderService.getMe()
            } catch (e: HttpException) {
                Log.d("getUserInfo_error", e.toString())
            }
        }
    }
}
