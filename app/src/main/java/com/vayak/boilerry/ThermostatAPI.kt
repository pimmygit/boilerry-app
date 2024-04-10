package com.vayak.boilerry

interface ThermostatAPI {
    suspend fun setThermostatFromJson(jsonString: String) : Thermostat
}