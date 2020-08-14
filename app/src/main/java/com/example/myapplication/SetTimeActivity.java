package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.Calendar;
import java.util.Timer;

import androidx.appcompat.app.AppCompatActivity;

public class SetTimeActivity extends AppCompatActivity {

    Context context;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_time);

        context = this;

        final NumberPicker hpicker = (NumberPicker) findViewById(R.id.hpicker);
        hpicker.setMinValue(0);
        hpicker.setMaxValue(2);
        hpicker.setWrapSelectorWheel(false);

        final NumberPicker mpicker = (NumberPicker) findViewById(R.id.mpicker);
        mpicker.setMinValue(0);
        mpicker.setMaxValue(59);

        if(hpicker.getValue() == 2) {
            mpicker.setDisplayedValues(new String[] {"0"});
        }
        mpicker.setDisplayedValues(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                                                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                                                "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
                                                "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
                                                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
                                                "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"});
        mpicker.setWrapSelectorWheel(false);

        Button positive = (Button) findViewById(R.id.button_start);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hour = String.valueOf(hpicker.getValue());
                String min = String.valueOf(mpicker.getValue());

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("hours", hour);
                intent.putExtra("minutes", min);

                startActivityForResult(intent, 101);
            }
        });

        Button negative = (Button) findViewById(R.id.button_cancel);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void countDown(String hour, String min) {
        long conversionTime = 0;
        conversionTime = Long.valueOf(hour) * 1000 * 3600 + Long.valueOf(min) * 60 * 1000;
        countDownTimer = new CountDownTimer(conversionTime, 60000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
}
