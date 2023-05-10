package com.capstone.adsrider.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.model.User
import com.capstone.adsrider.service.AdsRiderService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserLoginViewModel: ViewModel() {
    private val adsRiderService = AdsRiderService()
    private val _user = MutableStateFlow<User?>(null)

    private val user get() = _user

    fun login(email: String, passwd: String) {
        viewModelScope.launch {
            user.value = adsRiderService.login(email, passwd)
        }
    }
    fun signin(email: String, passwd: String) {
        viewModelScope.launch {
            user.value = adsRiderService.signin(email, passwd)
        }
    }
}
