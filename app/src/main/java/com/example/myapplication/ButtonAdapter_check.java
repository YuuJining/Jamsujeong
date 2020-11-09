package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.ReservationModel;
import model.SeatModel;

public class ButtonAdapter_check extends BaseAdapter {

    private DatabaseReference seat = FirebaseDatabase.getInstance().getReference().child("seat");
    private DatabaseReference reservation = FirebaseDatabase.getInstance().getReference().child("reservation");
    SeatModel seatModel;
    ReservationModel reservationModel;

    SimpleDateFormat hourFormat = new SimpleDateFormat("hh");
    SimpleDateFormat minFormat = new SimpleDateFormat("mm");
    long now;
    long leftTime;
    int leftHour = 0;
    int leftMin = 0;

    Context context = null;
    String[] ButtonNames = null;
    Integer[] ButtonIds = null;
    private LayoutInflater thisInflater;
    int seatNum;
    int seatId;

    public ButtonAdapter_check(Context context, String[] Buttons, Integer[] ButtonIds) {
        this.context = context;
        this.thisInflater = LayoutInflater.from(context);
        ButtonNames = Buttons;
        this.ButtonIds = ButtonIds;
    }

    @Override
    public int getCount() {
        return (ButtonNames != null) ? ButtonNames.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return ButtonNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getSeatsId(int position) {
        return ButtonIds[position];
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Button button = null;

        if (convertView != null) {
            button = (Button) convertView;
            button.setBackgroundResource(R.drawable.button_background_circle);
            button.setPadding(8, 8, 8, 8);
        } else {

            convertView = thisInflater.inflate(R.layout.grid_item, parent, false);
            ImageView button_default = (ImageView) convertView.findViewById(R.id.button_background);

            button = new Button(context);
            button.setText(ButtonNames[position]);
            button.setBackgroundResource(R.drawable.button_background_circle);
            button.setPadding(8, 8, 8, 8);
            
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seatNum = (int) getItemId(position) + 1;
                    seatId = getSeatsId(position);

                    reservation.child(String.valueOf(seatId)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            reservationModel = dataSnapshot.getValue(ReservationModel.class);

                            String seatName = "seat" + seatId;
                            seat.child(seatName).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    seatModel = dataSnapshot.getValue(SeatModel.class);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    if (seatModel.seatflag == true) {
                                        now = System.currentTimeMillis ( );
                                        if(reservationModel.endTime > now) {
                                            leftTime = reservationModel.endTime - now;
                                            leftHour = (int) leftTime / 3600000;
                                            leftMin =  (int) leftTime / 60000;
                                            builder.setMessage("좌석 " + seatNum + ": " + "사용 중" + "\n" + leftHour + "시간 " + leftMin + "분 남았습니다.")
                                                    .setNeutralButton("확 인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Toast.makeText(context, "확인 버튼이 눌렸습니다.", Toast.LENGTH_LONG).show();
                                                        }
                                                    }).create().show();
                                        }
                                    } else {
                                        builder.setMessage("좌석 " + seatNum + ": 사용 가능")
                                                .setNeutralButton("확 인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Toast.makeText(context, "확인 버튼이 눌렸습니다.", Toast.LENGTH_LONG).show();
                                                    }
                                                }).create().show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
        }

        return button;
    }
}