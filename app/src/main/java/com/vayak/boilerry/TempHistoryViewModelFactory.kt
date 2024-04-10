package com.vayak.boilerry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TempHistoryViewModelFactory(): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TempHistoryViewModel::class.java)) {
            return TempHistoryViewModel() as T
        }
        throw IllegalArgumentException("Failed to create factory due to invalid start arguments.")
    }
}