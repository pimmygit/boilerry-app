package com.vayak.boilerry

data class TempHistory(
    /*
    This is what the TempHistory object used to look like with the idea that it will be
    retrieved on demand...
    val sensor: String,
    val period_start: Int,
    val period_end: Int,
    val temp_history: List<TempMeasurement>
     */
    val datetime: String,
    val time_state_on: Int,
    val unit_speed: String,
    val unit_temperature: String,
    val temperature: Float? = null,
    val windchill: Float? = null,
    val wspd: Float? = null,
    val sensor_1: Float? = null,
    val sensor_2: Float? = null,
    val sensor_3: Float? = null
)
