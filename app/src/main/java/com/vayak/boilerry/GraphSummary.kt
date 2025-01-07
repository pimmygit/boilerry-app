package com.vayak.boilerry

import android.util.Log
import android.view.View
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class GraphSummary {

    companion object {

        fun drawGraphSummary(combinedChart: CombinedChart, tempHistoryData: ArrayList<TempHistory>) {
            //Part1
            val entriesIn = ArrayList<Entry>()
            val entriesOut = ArrayList<Entry>()
            val entriesOn = ArrayList<BarEntry>()

            Log.d("onCreate", "Number of graph records: ${tempHistoryData.size}")

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
    }
}