package com.vayak.boilerry

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ActivityDailySchedule : AppCompatActivity() {

    private lateinit var mToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_schedule)

        val dailyTemperatureScheduleTable: TableLayout? = findViewById(R.id.dailyTemperatureSchedule)

        val dailyTemperatures: List<Triple<Double, String, String>> = listOf(
            Triple(17.0, "00:00", "06:00"),
            Triple(21.0, "06:00", "08:30"),
            Triple(19.0, "08:30", "16:30"),
            Triple(21.0, "16:30", "22:00"),
            Triple(17.0, "22:00", "24:00")
        )

        // Print each tuple
        for (dailyTemperature in dailyTemperatures) {
            Log.d("onCreate", "Adding schedule [${dailyTemperature.second}]-[${dailyTemperature.third}]")

            val tableRowTemperatureTimeSlot = TableRow(this).apply {
                setBackgroundColor(Color.parseColor("#F0F7F7"))
                setPadding(5, 5, 5, 5)
            }

            val tableColumnFrom = TextView(this).apply {
                text = dailyTemperature.second
                layoutParams = TableRow.LayoutParams(50, TableRow.LayoutParams.WRAP_CONTENT, 1F);
            }

            val tableColumnTo = TextView(this).apply {
                text = dailyTemperature.third
                layoutParams = TableRow.LayoutParams(50, TableRow.LayoutParams.WRAP_CONTENT, 1F);
            }

            val tableColumnTemperature = TextView(this).apply {
                text = "${dailyTemperature.first}* C"
                layoutParams = TableRow.LayoutParams(30, TableRow.LayoutParams.WRAP_CONTENT, 1F);
            }

            val tableColumnButton = Button(this).apply {
                text = "Del"
                layoutParams = TableRow.LayoutParams(30, TableRow.LayoutParams.WRAP_CONTENT, 1F);
            }
            tableColumnButton.setOnClickListener {
                // Confirmation dialog before deleting the entry.
                val builder = AlertDialog.Builder(this@ActivityDailySchedule)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // Confirmed -> Remove the current row from the table
                        // todo: Prior that, we have to also remove the entry from the DB
                        val container = (tableRowTemperatureTimeSlot.parent as ViewGroup)
                        container.removeView(tableRowTemperatureTimeSlot)
                        container.invalidate()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }

            tableRowTemperatureTimeSlot.addView(tableColumnFrom)
            tableRowTemperatureTimeSlot.addView(tableColumnTo)
            tableRowTemperatureTimeSlot.addView(tableColumnTemperature)
            tableRowTemperatureTimeSlot.addView(tableColumnButton)

            dailyTemperatureScheduleTable?.addView(tableRowTemperatureTimeSlot)
        }

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
