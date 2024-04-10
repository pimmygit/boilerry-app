package com.vayak.boilerry

class TempHistoryRepository(private val tempHistoryAPI: TempHistoryAPI) {
    suspend fun setTemperatureFromJson(jsonString: String): TempHistory {
        return tempHistoryAPI.setTemperatureFromJson(jsonString)
    }
}