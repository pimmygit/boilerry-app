package com.vayak.boilerry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ThermostatViewModelFactory(): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThermostatViewModel::class.java)) {
            return ThermostatViewModel() as T
        }
        throw IllegalArgumentException("Failed to create factory due to invalid start arguments.")
    }
}