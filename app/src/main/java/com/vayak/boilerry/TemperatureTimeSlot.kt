package com.vayak.boilerry

data class TemperatureTimeSlot(
    // Default temperature is 17 degrees
    val temperature: Double = 17.0,
    // Minutes since 00:00 (min 0, max 1440)
    val timeStart: Int = 0,
    // Minutes since 00:00 (min 0, max 1440)
    val timeEnd: Int = 1439
)
