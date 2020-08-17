package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

public class SetTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_time);

        NumberPicker hpicker = (NumberPicker) findViewById(R.id.hpicker);
        hpicker.setMinValue(0);
        hpicker.setMaxValue(2);
        hpicker.setWrapSelectorWheel(false);

        NumberPicker mpicker = (NumberPicker) findViewById(R.id.mpicker);
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

        Button positive = (Button) findViewById(R.id.positiveButton);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        Button negative = (Button) findViewById(R.id.negativeButton);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
