package com.capstone.adsrider.main.buyticket

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.service.AdsRiderService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BuyTicketViewModel : ViewModel() {
    private val adsRiderService = AdsRiderService()
    private val _state = MutableStateFlow("")

    val state: MutableStateFlow<String>
        get() = _state

    fun setState(str: String) {
        _state.value = str
    }

    fun buyTicket(day: Int) {
        viewModelScope.launch {
            try {
                adsRiderService.buyTicket(day)
                _state.value = "success"
            } catch (e : HttpException) {
                Log.d("buyTicket_error", e.toString())
                _state.value = "fail"
            }
        }
    }
}
