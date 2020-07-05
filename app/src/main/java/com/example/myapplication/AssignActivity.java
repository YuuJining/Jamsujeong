package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

public class AssignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);

        NumberPicker hpicker = (NumberPicker) findViewById(R.id.hpicker);
        hpicker.setMinValue(0);
        hpicker.setMaxValue(2);
        hpicker.setWrapSelectorWheel(false);

        NumberPicker mpicker = (NumberPicker) findViewById(R.id.mpicker);
        mpicker.setMinValue(0);
        mpicker.setMaxValue(6);
        mpicker.setDisplayedValues(new String[]{"0", "10", "20", "30", "40", "50", "60"});
        mpicker.setWrapSelectorWheel(false);

        Button positive = (Button) findViewById(R.id.positiveButton);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
