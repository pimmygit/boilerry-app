package com.vayak.boilerry

import android.util.Log
import com.google.gson.Gson

class TempHistoryImpl : TempHistoryAPI {

    override suspend fun setTemperatureFromJson(jsonString: String): TempHistory {
        val tempHistoryObj = Gson().fromJson(jsonString, TempHistory::class.java)
        Log.d("ThermostatImpl", "OBJ: $tempHistoryObj")
        return tempHistoryObj
    }
}