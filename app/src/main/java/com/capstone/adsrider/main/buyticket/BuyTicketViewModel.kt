package com.capstone.adsrider.main.buyticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.service.AdsRiderService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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
            val user = adsRiderService.buyTicket(day)

            if (user == null) {
                state.value = "fail"
            }
            state.value = "success"
        }
    }
}
