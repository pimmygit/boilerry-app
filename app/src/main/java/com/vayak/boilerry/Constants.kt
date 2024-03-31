package com.vayak.boilerry

class Constants {
    companion object {
        const val HTTPOBJ_GET_STATE = "getstate"
        const val HTTPOBJ_THERMO_SWITCH = "thermo_switch"
        const val HTTPOBJ_THERMO_STATE = "thermo_state"
        const val HTTPOBJ_THERMO_TEMPERATURE = "thermo_temperature"
        const val HTTPOBJ_TEMPERATURE_ROOM = "temperature_room"

        const val MASTER_SWITCH_STATE_OFF = 0
        const val MASTER_SWITCH_STATE_ON = 1
        const val MASTER_SWITCH_STATE_TIMER = 2
        const val MASTER_SWITCH_STATE_PREDICTIVE = 3
    }
}