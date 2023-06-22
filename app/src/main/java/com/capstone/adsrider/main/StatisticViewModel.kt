package com.capstone.adsrider.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.adsrider.model.Statistic
import com.capstone.adsrider.service.AdsRiderService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StatisticViewModel : ViewModel() {
    private val adsRiderService = AdsRiderService()

    private val _statistic = MutableStateFlow(emptyList<Statistic>())
    val statistic get() = _statistic


    fun getStatistic(from: String, to: String) {
        viewModelScope.launch {
            try {
                _statistic.value = adsRiderService.getStatistic(from, to)
            } catch (e: HttpException) {
                Log.d("statistic_err", e.toString())
            }
        }
    }
}
