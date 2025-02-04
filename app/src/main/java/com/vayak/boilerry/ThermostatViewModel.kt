package com.vayak.boilerry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThermostatViewModel() : ViewModel() {
    private var _socketStatus = MutableLiveData(false)
    val thermostatLiveData = MutableLiveData<Thermostat>()

    private var _thermostatRepository = ThermostatRepository(ThermostatImpl())

    fun setStateFromJson(wsResponse: Pair<Boolean, String>) : LiveData<Thermostat> {

        viewModelScope.launch {
            if (_socketStatus.value == true) {
                Log.d("ThermostatViewModel", "JSON: ${wsResponse.second}")
                val thermostatData: Thermostat = withContext(Dispatchers.IO) {
                    _thermostatRepository.setThermostatFromJson(wsResponse.second)
                }
                thermostatLiveData.value = thermostatData
            }
        }
        return thermostatLiveData
    }

    fun setStatus(status: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }
}