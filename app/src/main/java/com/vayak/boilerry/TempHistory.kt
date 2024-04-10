package com.vayak.boilerry

data class TempHistory (
    val sensor: String,
    val period_start: Int,
    val period_end: Int,
    val temp_history: List<TempMeasurement>
)