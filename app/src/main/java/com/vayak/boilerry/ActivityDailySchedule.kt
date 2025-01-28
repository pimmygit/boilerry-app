package com.vayak.boilerry

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ActivityDailySchedule : AppCompatActivity() {

    private lateinit var mToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_schedule)

        // Initialise here to speed up the pop-up refresh
        mToast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT)

        val pickerFrom = findViewById<View>(R.id.time_picker_from) as TimePicker
        pickerFrom.setIs24HourView(true)
        val pickerTo = findViewById<View>(R.id.time_picker_to) as TimePicker
        pickerTo.setIs24HourView(true)

        val seekbarFieldTimeSlotTemperature = findViewById<SeekBar>(R.id.seekTimeSlotTemperature)
        // Start listening for user action on the thermostat setting
        seekbarFieldTimeSlotTemperature.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                //if (blockOnChangeListener != true) {
                //    Log.d("onCreate", "Sending command: setThermostat(${seekBar.progress})")
                //    webSocket?.send(setThermostatTemperature(seekBar.progress).toString())
                //}
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // Popup the current value as a message
                mToast.setText(progress.toString())
                mToast.show()
            }
        })
    }
}
