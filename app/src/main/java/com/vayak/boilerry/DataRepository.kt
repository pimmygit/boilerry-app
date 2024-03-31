package com.vayak.boilerry

class DataRepository(private val thermostatAPI: ThermostatAPI) {
    suspend fun setThermostatFromJson(jsonString: String): Thermostat {
        return thermostatAPI.setThermostatFromJson(jsonString)
    }
}