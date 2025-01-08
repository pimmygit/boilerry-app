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

public class ActivityDailySchedule extends AppCompatActivity {

    private static String[] timerValuesWith30MinSpan = new String[] {
            "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30", "06:00",
            "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
            "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00",
            "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "00:00"
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeslot);

    }
}
