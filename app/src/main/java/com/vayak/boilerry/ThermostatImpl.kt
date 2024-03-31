package com.vayak.boilerry

import android.util.Log
import com.google.gson.Gson

class ThermostatImpl : ThermostatAPI {

    override suspend fun setThermostatFromJson(jsonString: String): Thermostat {
        var thermobj = Gson().fromJson(jsonString, Thermostat::class.java)
        Log.d("ThermostatImpl", "OBJ: $thermobj}")
        return thermobj
    }
}