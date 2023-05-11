package com.capstone.adsrider.main.rentbike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.model.Ad
import com.capstone.adsrider.model.Riding
import com.capstone.adsrider.service.AdsRiderService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AdsExposureViewModel: ViewModel() {
    private val adsRiderService = AdsRiderService()
    private val _state = MutableStateFlow("")

    val state get() = _state

    fun ridingComplete(riding: Riding) {
        viewModelScope.launch {
            state.value = adsRiderService.ridingComplete(riding)!!
        }
    }
}
