package com.vayak.boilerry

data class DailySchedule(
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
    val dayOfWeek: String,
    val timeSlots: ArrayList<TemperatureTimeSlot>,
    val unit_speed: String,
    val unit_temperature: String,
    val temperature: Float? = null,
    val windchill: Float? = null,
    val wspd: Float? = null,
    val sensor_1: Float? = null,
    val sensor_2: Float? = null,
    val sensor_3: Float? = null
)
