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
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ActivityMain : AppCompatActivity() {

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
    private lateinit var viewModel:             ThermostatViewModel

    private lateinit var mToast:                Toast;

    @SuppressLint("UseSwitchCompatOrMaterialCode", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreate", "Starting")
        super.onCreate(savedInstanceState)

        // TODO: Do it properly
        var graphData = ArrayList<TempHistory>()
        var graphBuilt = false

        // Initialise here to speed up the pop-up refresh
        mToast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("onCreate", "Created binding")
        viewModel = ViewModelProvider(this)[ThermostatViewModel::class.java]

        viewModel.thermostatLiveData.observe(this) {
            blockOnChangeListener = true
            (binding.radioThermoSwitch.getChildAt(it.thermo_switch) as RadioButton).isChecked = true

            // Current temperature
            binding.temperatureRoom.text = buildString {
                append(it.temp_now.toString())
                append(" C")
            }

            // Thermostat manual temperature setting
            binding.seekThermoTemperature.progress = it.thermo_temperature
            binding.seekThermoTemperature.isEnabled = it.thermo_switch == 1

            // Current state of the heating (heater on/off)
            if (it.thermo_relay) {
                binding.thermoState.setImageResource(R.drawable.heating_on)
            } else {
                binding.thermoState.setImageResource(R.drawable.heating_off)
            }

            if (!graphBuilt) {
                // The graph data wont change really - there is need to re-build it every time.
                graphBuilt = true
                drawGraph(it.temp_history as ArrayList<TempHistory>)
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
    }

    fun String.toTimeDateLong(): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK)
        return format.parse(this)?.time ?: throw IllegalArgumentException("Invalid time string")
    }

    private fun drawGraph(tempHistoryData: ArrayList<TempHistory>) {
        //Part1
        val entriesIn = ArrayList<Entry>()
        val entriesOut = ArrayList<Entry>()
        val entriesOn = ArrayList<BarEntry>()

        Log.d("onCreate","Number of graph records: ${tempHistoryData.size}")

        var index = 1f
        val labelList = ArrayList<String>()
        for (tempMeasurement in tempHistoryData) {
            if (tempMeasurement.sensor_1 != null && tempMeasurement.temperature != null) {
                entriesIn.add(Entry(index, tempMeasurement.sensor_1))
                entriesOut.add(Entry(index, tempMeasurement.temperature))
                entriesOn.add(BarEntry(index, tempMeasurement.time_state_on.toFloat()))
                labelList.add(tempMeasurement.datetime)
                index++
            }
        }
        //Part2

        //Part3
        val ldsIn = LineDataSet(entriesIn, "Inside")
        ldsIn.axisDependency = AxisDependency.LEFT
        val ldsOut = LineDataSet(entriesOut, "Outside")
        ldsOut.axisDependency = AxisDependency.LEFT
        val ldsOn = BarDataSet(entriesOn, "Heating")
        ldsOn.axisDependency = AxisDependency.RIGHT

        //Part4
        ldsIn.setDrawValues(false)
        ldsIn.setDrawFilled(true)
        ldsIn.setDrawCircles(false)
        ldsIn.lineWidth = 3f
        //ldsIn.color = R.color.blue_ivy
        ldsIn.fillColor = R.color.alice_blue
        ldsIn.fillAlpha = 50

        ldsOut.setDrawValues(false)
        ldsOut.setDrawFilled(true)
        ldsOut.setDrawCircles(false)
        ldsOut.lineWidth = 3f
        ldsOut.color = R.color.blue
        ldsOut.fillColor = R.color.aqua
        ldsOut.fillAlpha = 100

        ldsOn.color = R.color.orange
        ldsOn.barBorderColor = R.color.orange_red

        //Part5
        val combinedChart = findViewById<View>(R.id.temperature_chart) as CombinedChart

        combinedChart.xAxis.labelRotationAngle = 0f

        //Part6
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(ldsIn)
        dataSets.add(ldsOut)

        val combinedData = CombinedData()
        combinedData.setData(LineData(dataSets))
        combinedData.setData(BarData(ldsOn))

        combinedChart.data = combinedData
        combinedChart.invalidate()

        // Add the datetime as labels
        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                return try {
                    labelList[value.toInt()].drop(8).dropLast(3)
                } catch (oob: IndexOutOfBoundsException) {
                    labelList[0].drop(8).dropLast(3)
                }
            }
        }
        val xAxis: XAxis = combinedChart.xAxis
        xAxis.granularity = 1f // minimum axis-step (interval) is 1
        xAxis.valueFormatter = formatter

        //Part7
        combinedChart.axisRight.isEnabled = false
        //lineChart.xAxis.axisMaximum = j+0.1f

        //Part8
        //lineChart.setTouchEnabled(true)
        //lineChart.setPinchZoom(true)

        //Part9
        combinedChart.description.text = "Days"
        combinedChart.setNoDataText("No forex yet!")

        //Part10
        combinedChart.animateX(1800, Easing.EaseInExpo)

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
