package com.vayak.boilerry

import com.vayak.boilerry.Constants.Companion.HTTPOBJ_THERMO_STATE
import com.vayak.boilerry.Constants.Companion.HTTPOBJ_TEMP_HISTORY
import com.vayak.boilerry.Constants.Companion.HTTPOBJ_TEMP_NOW
import com.vayak.boilerry.Constants.Companion.HTTPOBJ_THERMO_SWITCH
import com.vayak.boilerry.Constants.Companion.HTTPOBJ_THERMO_TEMPERATURE
import org.json.JSONObject

class JsonRequestStore {

    companion object {
        /*
        Retrieves all current values from the boiler controller.
        {
            "name": "thermo_state",
            "action": "get"
        }
        */
        fun getThermostatState(): JSONObject {
            val requestJson = JSONObject()
            requestJson.put("name", HTTPOBJ_THERMO_STATE)
            requestJson.put("action", "get")

            return requestJson
        }

        /*
        Checks if the system is in state On or Off
        {
            "name": "master_switch",
            "action": "get"
        }
        */
        fun getThermostatMasterSwitch(): JSONObject {
            val requestJson = JSONObject()
            requestJson.put("name", HTTPOBJ_THERMO_SWITCH)
            requestJson.put("action", "get")

            return requestJson
        }

        /*
        Switches the system On or Off
        {
            "name": "master_switch",
            "action": "set"
            "value": "<0,1,2,...>"
        }
        */
        fun setThermostatMasterSwitch(state: Int): JSONObject {
            val requestJson = JSONObject()
            requestJson.put("name", HTTPOBJ_THERMO_SWITCH)
            requestJson.put("action", "set")
            requestJson.put("value", state)

            return requestJson
        }

        /*
        Sets the thermostat to a temperature that the boiler should maintain.
        {
            "name": "thermo_temperature",
            "action": "set"
            "value": "<temperature>"
        }
        */
        fun setThermostatTemperature(temperature: Int?): JSONObject {
            val requestJson = JSONObject()
            requestJson.put("name", HTTPOBJ_THERMO_TEMPERATURE)
            requestJson.put("action", "set")
            requestJson.put("value", temperature)

            return requestJson
        }

        /*
        Gets the temperature to which the thermostat is set to.
        {
            "name": "thermo_temperature",
            "action": "get"
        }
        */
        fun getThermostatTemperature(): JSONObject {
            val requestJson = JSONObject()
            requestJson.put("name", HTTPOBJ_THERMO_TEMPERATURE)
            requestJson.put("action", "get")

            return requestJson
        }

        /*
        Retrieves the current room temperature
        {
            "name": "temperature_now",
            "action": "get"
            "value": "<sensor_id>"
        }
        */
        fun getTemperatureNow(sensor: String = "sensor_1"): JSONObject {
            val requestJson = JSONObject()
            requestJson.put("name", HTTPOBJ_TEMP_NOW)
            requestJson.put("action", "get")
            requestJson.put("value", sensor)

            return requestJson
        }

        /*
        Retrieves the temperature history
        {
            "name": "temperature_history",
            "action": "get",
            "value": "<sensor_id>"
        }
        */
        fun getTemperatureHistory(sensor: String = "sensor_1"): JSONObject {
            val requestJson = JSONObject()
            requestJson.put("name", HTTPOBJ_TEMP_HISTORY)
            requestJson.put("action", "get")
            requestJson.put("value", sensor)

            return requestJson
        }
    }
}