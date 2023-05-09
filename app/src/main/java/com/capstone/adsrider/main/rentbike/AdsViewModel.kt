package com.capstone.adsrider.main.rentbike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.model.Ad
import com.capstone.adsrider.service.AdsRiderService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AdsViewModel: ViewModel() {
    private val adsRiderService = AdsRiderService()
    private val _ads = MutableStateFlow(emptyList<Ad>())

    val ads: MutableStateFlow<List<Ad>>
        get() = _ads

    init {
        viewModelScope.launch {
            ads.value = adsRiderService.getAdsList()!!
        }
    }
}
