package com.vayak.boilerry

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.vayak.boilerry.Constants.Companion.MASTER_SWITCH_STATE_OFF
import com.vayak.boilerry.Constants.Companion.MASTER_SWITCH_STATE_ON
import com.vayak.boilerry.Constants.Companion.MASTER_SWITCH_STATE_PREDICTIVE
import com.vayak.boilerry.Constants.Companion.MASTER_SWITCH_STATE_TIMER
import com.vayak.boilerry.JsonRequestStore.Companion.setThermostatMasterSwitch
import com.vayak.boilerry.JsonRequestStore.Companion.setThermostatTemperature
import com.vayak.boilerry.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


class MainActivity : AppCompatActivity() {

    private var cHOST:                          String?             = "pimmy.ddns.net"
    //private var cHOST:                          String?             = "192.168.1.7"
    //private var cHOST:                          String?             = "92.13.183.194"
    private var cPORT:                          Int                 = 9741
    private val cPROTOCOL:                      String?             = "ws"
    private val cURL:                           String              = "$cPROTOCOL://$cHOST:$cPORT"

    private var blockOnChangeListener:          Boolean?            = false

    private val okHttpClient:                   OkHttpClient        = OkHttpClient()
    private var webSocket:                      WebSocket?          = null
    private lateinit var wsRequest:             Request
    private lateinit var wsListener:            WebSocketListener

    private lateinit var binding:               ActivityMainBinding
    private lateinit var viewModel:             MainViewModel

    private lateinit var mToast:                Toast;

    @SuppressLint("UseSwitchCompatOrMaterialCode", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreate", "Starting")
        super.onCreate(savedInstanceState)

        // Initialise here to speed up the pop-up refresh
        mToast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("onCreate", "Created binding")
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.thermostatLiveData.observe(this) {
            blockOnChangeListener = true
            (binding.radioThermoSwitch.getChildAt(it.thermo_switch) as RadioButton).isChecked = true

            // Current temperature
            binding.temperatureRoom.text = buildString {
                append(it.temperature_room.toString())
                append(" C")
            }

            // Thermostat manual temperature setting
            binding.seekThermoTemperature.progress = it.thermo_temperature
            binding.seekThermoTemperature.isEnabled = it.thermo_switch == 1

            // Current state of the heating (heater on/off)
            if (it.thermo_state) {
                binding.thermoState.setImageResource(R.drawable.heating_on)
            } else {
                binding.thermoState.setImageResource(R.drawable.heating_off)
            }
            blockOnChangeListener = false
        }
        Log.d("onCreate", "Created ViewModel")

        wsListener = WebSocketListener(viewModel)
        Log.d("onCreate", "Created WebsocketListener")

        wsRequest = Request.Builder().url(cURL).build()
        webSocket = okHttpClient.newWebSocket(wsRequest, wsListener)
        Log.d("onCreate", "Created WebSocket: $wsRequest")

        val seekbarFieldThermostatTemperature = findViewById<SeekBar>(R.id.seekThermoTemperature)
        // Start listening for user action on the thermostat setting
        seekbarFieldThermostatTemperature.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                if (blockOnChangeListener != true) {
                    Log.d("onCreate", "Sending command: setThermostat(${seekBar.progress})")
                    webSocket?.send(setThermostatTemperature(seekBar.progress).toString())
                }
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

        val buttonMasterSwitch = findViewById<RadioGroup>(R.id.radioThermoSwitch)
        // Start listening for user action on the Master switch button
        buttonMasterSwitch?.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    R.id.radioThermoSwitchOff -> {
                        if (blockOnChangeListener != true) {
                            Log.d("onCreate","Sending command: setMasterSwitch($MASTER_SWITCH_STATE_OFF)")
                            webSocket?.send(setThermostatMasterSwitch(MASTER_SWITCH_STATE_OFF).toString())
                            seekbarFieldThermostatTemperature.isEnabled = false
                            val thermoStateImage = findViewById<ImageView>(R.id.thermoState)
                            thermoStateImage.setImageResource(R.drawable.heating_off)
                        }
                    }
                    R.id.radioThermoSwitchOn -> {
                        if (blockOnChangeListener != true) {
                            Log.d("onCreate","Sending command: setMasterSwitch($MASTER_SWITCH_STATE_ON)")
                            webSocket?.send(setThermostatMasterSwitch(MASTER_SWITCH_STATE_ON).toString())
                            seekbarFieldThermostatTemperature.isEnabled = true
                        }
                    }
                    R.id.radioThermoSwitchTimer -> {
                        if (blockOnChangeListener != true) {
                            Log.d("onCreate","Sending command: setMasterSwitch($MASTER_SWITCH_STATE_TIMER)")
                            webSocket?.send(setThermostatMasterSwitch(MASTER_SWITCH_STATE_TIMER).toString())
                            seekbarFieldThermostatTemperature.isEnabled = false
                        }
                    }
                    R.id.radioThermoSwitchPredictive -> {
                        if (blockOnChangeListener != true) {
                            Log.d("onCreate","Sending command: setMasterSwitch($MASTER_SWITCH_STATE_PREDICTIVE)"                            )
                            webSocket?.send(setThermostatMasterSwitch(MASTER_SWITCH_STATE_PREDICTIVE).toString())
                            seekbarFieldThermostatTemperature.isEnabled = false
                        }
                    }
                }
            }
        }
        //Part1
        val entries = ArrayList<Entry>()

        //Part2
        entries.add(Entry(1f, 16f))
        entries.add(Entry(2f, 16.3f))
        entries.add(Entry(3f, 16.7f))
        entries.add(Entry(4f, 17f))
        entries.add(Entry(5f, 17.4f))
        entries.add(Entry(6f, 18f))
        entries.add(Entry(7f, 19f))
        entries.add(Entry(8f, 19f))
        entries.add(Entry(9f, 19f))
        entries.add(Entry(10f, 18f))
        entries.add(Entry(11f, 17f))
        entries.add(Entry(12f, 17f))
        entries.add(Entry(13f, 17f))
        entries.add(Entry(14f, 17.5f))
        entries.add(Entry(15f, 17.8f))
        entries.add(Entry(16f, 18.5f))
        entries.add(Entry(17f, 19f))
        entries.add(Entry(18f, 19.2f))
        entries.add(Entry(19f, 19f))
        entries.add(Entry(20f, 18.6f))
        entries.add(Entry(21f, 18.4f))
        entries.add(Entry(22f, 18.6f))
        entries.add(Entry(23f, 18.5f))
        entries.add(Entry(24f, 17.8f))

        //Part3
        val vl = LineDataSet(entries, "Temperature")

        //Part4
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.grey
        vl.fillAlpha = R.color.red

        //Part5
        val lineChart = findViewById<View>(R.id.temperature_chart) as LineChart

        lineChart.xAxis.labelRotationAngle = 0f

        //Part6
        lineChart.data = LineData(vl)

        //Part7
        lineChart.axisRight.isEnabled = false
        //lineChart.xAxis.axisMaximum = j+0.1f

        //Part8
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

        //Part9
        lineChart.description.text = "Days"
        lineChart.setNoDataText("No forex yet!")

        //Part10
        lineChart.animateX(1800, Easing.EaseInExpo)

        //Part11
        //val markerView = CustomMarker(this@ShowForexActivity, R.layout.marker_view)
        //lineChart.marker = markerView

    }

    //Called when the activity is about to become visible
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onStart() {
        super.onStart()

        //webSocket?.send(getState().toString())
    }

    //Called when the activity has become visible
    override fun onResume() {
        super.onResume()
    }

    //Called when another activity is taking focus
    override fun onPause() {
        super.onPause()
    }

    //Called before the activity is destroyed
    public override fun onDestroy() {
        super.onDestroy()
        webSocket?.close(1000, "Android application closed by user.")
        okHttpClient.dispatcher.executorService.shutdown()
    }
}
