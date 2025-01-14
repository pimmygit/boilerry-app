package com.vayak.boilerry

import android.util.Log

data class DailySchedule(
    val dayOfWeek: String,
    val timeSlots: ArrayList<TemperatureTimeSlot>
) {
    /*
    Object containing the temperatures for various time slots throughout a given day.

    There are two constraint for the day time slots:
        1. They cannot have gaps between them.
        2. They cannot overlap in time.
    For example:
        Valid:      00:00-07:59; 08:00-17:00; 17:01-11:59
        Invalid:    00:00-07:59; 17:01-11:59 (gap between 07:59 and 17:01)
        Invalid:    00:00-08:00; 08:00-17:00; 17:01-11:59 (overlap by a minute at 08:00)
    Workflow:
        1. We start with a single time slot with default temperature of 17 degrees: [00:00-11:59].
        2. If the user adds a time slot of 21 degrees for 08:00-17:00, the code will determine that
        this slot fits into a single parent slot. It will therefore break the parent slot [00:00-11:59]
        into three separate slots [00:00-07:59; 08:00-17:00; 17:01-11:59].
        3. Similarly, if the user deletes a slot, the code will join the neighbouring two slots,
        by making the one with lower temperature to extend on the place of the deleted time slot.
        4. If the user tries to add a time slot that overlaps two existing slots, an error will be reported.
    */

    fun addTimeSlot(
        temperature: Double = 17.0,
        timeStart: Int = 0,
        timeEnd: Int = 1439
    ): Int {
        // Check if the time slot is overlapping more than one parent time slot
        var indexOfOriginalTimeSlot = -1
        var previousTimeSlot:TemperatureTimeSlot = TemperatureTimeSlot()
        var newTimeSlot:TemperatureTimeSlot = TemperatureTimeSlot()
        var followingTimeSlot:TemperatureTimeSlot = TemperatureTimeSlot()

        Log.d("addTimeSlot", "Checking validity: temperature[$temperature], start[$timeStart], end[$timeEnd].")

        for (timeSlot: TemperatureTimeSlot in timeSlots) {
            // If the new time slot overlaps only with one existing time slot,
            // we break the original time slot to insert the new one.
            if ( (0 == timeStart || timeSlot.timeStart < timeStart) && (timeEnd < timeSlot.timeEnd || timeEnd == 1439 ) ) {
                indexOfOriginalTimeSlot = timeSlots.indexOf(timeSlot)

                Log.d("addTimeSlot", "Valid with index[$indexOfOriginalTimeSlot] and temperature[$temperature], start[$timeStart], end[$timeEnd].")

                // Create the new time slots in replacement to the original one.
                newTimeSlot = TemperatureTimeSlot(temperature=temperature, timeStart=timeStart, timeEnd=timeEnd)
                if (timeStart > 0) {
                    previousTimeSlot = TemperatureTimeSlot(temperature=timeSlot.temperature, timeStart=timeSlot.timeStart, timeEnd=timeStart-1)
                    Log.d("addTimeSlot", "Create previous with temperature[${previousTimeSlot.temperature}], start[${previousTimeSlot.timeStart}], end[${previousTimeSlot.timeEnd}].")
                }
                if (timeEnd < 1439) {
                    followingTimeSlot = TemperatureTimeSlot(temperature=timeSlot.temperature, timeStart=timeEnd+1, timeEnd=timeSlot.timeEnd)
                    Log.d("addTimeSlot", "Create following with temperature[${followingTimeSlot.temperature}], start[${followingTimeSlot.timeStart}], end[${followingTimeSlot.timeEnd}].")
                }
                break
            } else {
                Log.d("addTimeSlot", "Invalid with temperature[${timeSlot.temperature}], start[${timeSlot.timeStart}], end[${timeSlot.timeEnd}].")
            }
        }

        // Have we successfully found replacement to the old time slot with the new one?
        if (newTimeSlot == TemperatureTimeSlot()) {
            return 1
        }
        // Remove the original time slot and place the new ones
        Log.d("addTimeSlot", "Removing time slot with index: $indexOfOriginalTimeSlot")
        timeSlots.removeAt(indexOfOriginalTimeSlot)

        Log.d("addTimeSlot", "Adding new time slots..")
        timeSlots.add(newTimeSlot)
        if (previousTimeSlot != TemperatureTimeSlot()) {
            timeSlots.add(previousTimeSlot)
        }
        if (followingTimeSlot != TemperatureTimeSlot()) {
            timeSlots.add(followingTimeSlot)
        }
        return 0
    }

    fun removeTimeSlot(temperature: Double, timeStart: Int, timeEnd: Int): Int {
        val timeSlotToRemove = TemperatureTimeSlot(temperature, timeStart, timeEnd)
        val indexOfTimeSlotToRemove = timeSlots.indexOf(timeSlotToRemove)

        if (indexOfTimeSlotToRemove > -1) {
            Log.d("removeTimeSlot", "Removing time slot with index: $indexOfTimeSlotToRemove")
            timeSlots.removeAt(indexOfTimeSlotToRemove)
            return 0
        } else {
            Log.d("removeTimeSlot", "Failed to identify time slot with index: $indexOfTimeSlotToRemove")
        }

        return 1
    }
}

