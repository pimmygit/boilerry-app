package com.vayak.boilerry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vayak.boilerry.Constants.Companion.HTTPOBJ_THERMO_STATE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class TempHistoryViewModel() : ViewModel() {
    private var _socketStatus = MutableLiveData(false)
    val tempHistoryLiveData = MutableLiveData<TempHistory>()

    private var _tempHistoryRepository = TempHistoryRepository(TempHistoryImpl())

    fun setStateFromJson(wsResponse: Pair<Boolean, String>) : LiveData<TempHistory> {
        viewModelScope.launch {

            if (_socketStatus.value == true) {
                Log.d("MainViewModel", "JSON: ${wsResponse.second}")
                val tempHistoryData: TempHistory = withContext(Dispatchers.IO) {
                    _tempHistoryRepository.setTemperatureFromJson(wsResponse.second)
                }
                tempHistoryLiveData.value = tempHistoryData
            }
        }
        return tempHistoryLiveData
    }

    fun setStatus(status: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }
}