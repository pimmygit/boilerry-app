package com.vayak.boilerry

data class Thermostat(
    val thermo_switch: Int,
    val thermo_temperature: Int,
    val thermo_relay: Boolean,
    val temp_now: Float,
    val temp_history: List<TempMeasurement>
)
