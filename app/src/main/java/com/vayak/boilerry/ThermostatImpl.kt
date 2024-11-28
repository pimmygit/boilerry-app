package com.vayak.boilerry

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException


class FloatAdapter : TypeAdapter<Float?>() {
    @Throws(IOException::class)
    override fun read(reader: JsonReader): Float? {
        if (reader.peek() === JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        val strval: String = reader.nextString()
        return try {
            val floatval = strval.toFloat()
            floatval
        } catch (nfe: NumberFormatException) {
            null
        }
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, value: Float?) {
        if (value == null) {
            writer.nullValue()
            return
        }
        writer.value(value)
    }
}


class ThermostatImpl : ThermostatAPI {

    override suspend fun setThermostatFromJson(jsonString: String): Thermostat {
        val builder = GsonBuilder();
        builder.registerTypeAdapter(java.lang.Float::class.java, FloatAdapter());
        val gson = builder.create();

        val thermobj = gson.fromJson(jsonString, Thermostat::class.java)
        Log.d("ThermostatImpl", "OBJ: $thermobj")
        return thermobj
    }
}