package com.vayak.boilerry

interface ThermostatAPI {
    suspend fun setThermostatFromJson(JsonString: String) : Thermostat
}