package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.core.Tag;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import model.ReservationModel;

public class SetTimeActivity extends AppCompatActivity {

    Context context;
    int hour;
    int min;
    Calendar pickerTime = Calendar.getInstance();
    long usingTime;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    NumberPicker hpicker;
    NumberPicker mpicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_time);

        context = this;

        hpicker = (NumberPicker) findViewById(R.id.hpicker);
        hpicker.setMinValue(0);
        hpicker.setMaxValue(2);
        mpicker = (NumberPicker) findViewById(R.id.mpicker);
        mpicker.setMinValue(0);
        mpicker.setMaxValue(59);

        mpicker.setDisplayedValues(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                                                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                                                "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
                                                "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
                                                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
                                                "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"});
        mpicker.setWrapSelectorWheel(false);

        hpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal == 2){
                    mpicker.setValue(0);
                    mpicker.setEnabled(false);
                } else mpicker.setEnabled(true);

                hour = newVal;
                Log.v("check", "hour : " + hour);
                pickerTime.set(Calendar.HOUR_OF_DAY, hour);
            }
        });

        mpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                min = newVal;
                Log.v("check", "min : " + min);
                pickerTime.set(Calendar.MINUTE, min);
            }
        });


        Log.v("check", "pickertime : " + pickerTime);
        usingTime = pickerTime.getTimeInMillis();
        Log.v("check", "usingtime : " + usingTime);


        Button positive = (Button) findViewById(R.id.button_start);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent passedIntent = getIntent();
                addReservationData(usingTime, passedIntent);
                setSeatFlagTrue(passedIntent);
                setUserFlagTrue();

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


    public void addReservationData(long time, Intent intent) {
        int seatNum = intent.getIntExtra("seatId", 100);

        ReservationModel reservation = new ReservationModel();
        reservation.uid = firebaseAuth.getInstance().getCurrentUser().getUid();
        reservation.alert = true;
        reservation.startTime = ServerValue.TIMESTAMP;
        reservation.endTime = (long) reservation.startTime + (long) reservation.setTime;
        reservation.setTime = time;

        String seatId = String.valueOf(seatNum);
        FirebaseDatabase.getInstance().getReference().child("reservation").child(seatId).setValue(reservation);
    }

    public void setSeatFlagTrue(Intent intent) {
        String num = String.valueOf(intent.getIntExtra("seatId", 100));
        String seatNum = "seat" + num;
        FirebaseDatabase.getInstance().getReference().child("seat").child(seatNum).child("seatFlag").setValue(true);
    }

    public void setUserFlagTrue() {
        String uid = firebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("flag").setValue(true);
    }
}
