package com.capstone.adsrider.main.swapcoin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.model.Balance
import com.capstone.adsrider.service.AdsRiderService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SwapCoinViewModel : ViewModel() {
    private val adsRiderService = AdsRiderService()

    private val _balance = MutableStateFlow<Balance?>(null)
    private val _state = MutableStateFlow("")

    val balance get() = _balance

    val state get() = _state

    fun setState(str: String) {
        _state.value = str
    }

    fun purchaseCoin(amount: String) {
        viewModelScope.launch {
            try {
                _balance.value = adsRiderService.purchaseCoin(amount)
                _state.value = "success"
            } catch (e: HttpException) {
                Log.d("balance_err", e.toString())
                _state.value = "fail"
            }
        }
    }
}
