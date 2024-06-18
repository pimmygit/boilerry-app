package com.vayak.boilerry;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.seosh817.circularseekbar.CircularSeekBar;
import com.seosh817.circularseekbar.callbacks.OnAnimationEndListener;
import com.seosh817.circularseekbar.callbacks.OnProgressChangedListener;

public class ActivityTimeSlot extends AppCompatActivity {

    private static String[] timerValuesWith30MinSpan = new String[] {
            "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30", "06:00",
            "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
            "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00",
            "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "00:00"
    };

    private NumberPicker fromTimePicker;
    private NumberPicker toTimePicker;

    private CircularSeekBar circularSeekBar;

    private TextView tvProgressValue;
    private String[] fromPickerVals = timerValuesWith30MinSpan;
    private String[] toPickerVals = timerValuesWith30MinSpan;

    //private lateinit ActivityMainBinding binding



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeslot);

        //ActivityMainBinding binding = ActivityMainBinding.inflate(layoutInflater);

        circularSeekBar = findViewById(R.id.circular_seek_bar);
        //circularSeekBar.set
        tvProgressValue = findViewById(R.id.tv_progress_value);


        fromTimePicker = findViewById(R.id.from_time_picker);
        fromTimePicker.setMaxValue(timerValuesWith30MinSpan.length -1);
        fromTimePicker.setMinValue(0);
        //pickerVals  = new String[] {"dog", "cat", "lizard", "turtle", "axolotl"};
        fromTimePicker.setDisplayedValues(fromPickerVals);
        toTimePicker = findViewById(R.id.to_time_picker);
        toTimePicker.setMaxValue(timerValuesWith30MinSpan.length -1);
        toTimePicker.setMinValue(0);
        toTimePicker.setDisplayedValues(fromPickerVals);

        fromTimePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = fromTimePicker.getValue();
                Log.d("From Timer picker value", fromPickerVals[valuePicker1]);
            }
        });


        toTimePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = toTimePicker.getValue();
                Log.d("To Timer picker value", toPickerVals[valuePicker1]);
            }
        });

        /*with(binding) {
            circularSeekBar.setOnProgressChangedListener { progress ->
                    tvProgressValue.text = progress
                            .roundToInt()
                            .toString()
            }

            circularSeekBar.setOnAnimationEndListener { _ ->
                // listen
            }
        }
*/

        circularSeekBar.setOnProgressChangedListener(new OnProgressChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(float v) {
                //circularSeekBar.setProgress(20.00f);
                tvProgressValue.setText(Float.toString(v));
            }
        });

       circularSeekBar.setOnAnimationEndListener(new OnAnimationEndListener(){
           @Override
           public void onAnimationEnd(float v) {
               //doNothing
           }
       });

    }
}
