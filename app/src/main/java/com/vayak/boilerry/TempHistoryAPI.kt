package com.vayak.boilerry

interface TempHistoryAPI {
    suspend fun setTemperatureFromJson(jsonString: String) : TempHistory
}