package com.vayak.boilerry

class ThermostatRepository(private val thermostatAPI: ThermostatAPI) {
    suspend fun setThermostatFromJson(jsonString: String): Thermostat {
        return thermostatAPI.setThermostatFromJson(jsonString)
    }
}