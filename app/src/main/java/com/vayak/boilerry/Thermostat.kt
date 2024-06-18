package com.vayak.boilerry

data class Thermostat(
    val thermo_relay: Boolean,
    val thermo_switch: Int,
    val thermo_temperature: Int,
    val temp_now: Float?,
    val temp_history: List<TempHistory>
)
