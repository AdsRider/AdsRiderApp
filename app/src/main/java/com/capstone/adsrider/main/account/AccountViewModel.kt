package com.capstone.adsrider.main.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.model.Balance
import com.capstone.adsrider.model.History
import com.capstone.adsrider.service.AdsRiderService
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AccountViewModel : ViewModel() {
    private val adsRiderService = AdsRiderService()

    private val _balance = MutableStateFlow(emptyList<Balance>())
    val balance get() = _balance

    private val _history = MutableStateFlow(emptyList<History>())
    val history get() = _history

    private val _hash = MutableStateFlow("")
    val hash get() = _history

    fun getBalance() {
        viewModelScope.launch {
            try {
                _balance.value = adsRiderService.getBalance()!!
            } catch (e: HttpException) {
                Log.d("balance_err", e.toString())
            }
        }
    }

    fun stopAccount() {
        viewModelScope.cancel()
    }

    fun getHistory() {
        viewModelScope.launch {
            try {
                _history.value = adsRiderService.getHistory()!!
            } catch (e: HttpException) {
                Log.d("history_err", e.toString())
            }
        }
    }

    fun withdrawal(to: String, amount: String) {
        viewModelScope.launch {
            try {
                _hash.value = adsRiderService.withdrawal(to, amount)!!
                Log.d("withdrawal_success", _hash.value)
            } catch (e: HttpException) {
                Log.d("withdrawal_err", e.toString())
            }
        }
    }
}
