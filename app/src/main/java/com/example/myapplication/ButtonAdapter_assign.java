package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import model.SeatModel;

public class ButtonAdapter_assign extends BaseAdapter {
    Context context = null;
    String[] ButtonNames = null;
    Integer[] ButtonIds = null;
    private LayoutInflater thisInflater;
    private DatabaseReference seat = FirebaseDatabase.getInstance().getReference().child("seat");
    SeatModel seatModel;


    public ButtonAdapter_assign(Context context, String[] Buttons, Integer[] ButtonIds) {
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
    public long getItemId(int position) { return position; }

    public int getSeatsId(int position) { return ButtonIds[position];}

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Button button = null;

        if(convertView != null) {
            button = (Button)convertView;
            button.setBackgroundResource(R.drawable.grid_item);
            button.setLayoutParams(new ViewGroup.LayoutParams(120, 120));
            button.setPadding(8, 8, 8, 8);
        } else {
            button.setBackgroundResource(R.drawable.grid_item);
            button.setLayoutParams(new ViewGroup.LayoutParams(120, 120));
            button.setPadding(8, 8, 8, 8);

            button = new Button(context);
            button.setText(ButtonNames[position]);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int seatNum = (int) getItemId(position) + 1;
                    final int seatId = getSeatsId(position);
                    String seatName = "seat" + seatId;
                    seat.child(seatName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            seatModel = dataSnapshot.getValue(SeatModel.class);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

//                    Toast.makeText(context,"좌석 " + getItemId(seatId) + "을 이용하겠습니까?", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("좌석 " + seatNum + "을 이용하시겠습니까?");

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(seatModel.seatflag == true) {
                                    Toast.makeText(context, "이미 사용 중인 좌석입니다.", Toast.LENGTH_LONG).show();
//                                    Intent intent = new Intent(context, SetTimeActivity.class);
//                                    intent.putExtra("seatId", seatId);
//                                    context.startActivity(intent);
                                } else {
                                    Toast.makeText(context, "좌석 " + seatNum + " 선택이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context, SetTimeActivity.class);
                                    intent.putExtra("seatId", seatId);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    context.startActivity(intent);
                                }

                            }
                    }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "동작이 취소되었습니다.", Toast.LENGTH_LONG).show();

                        }
                    }).create().show();
                }
            });
        }
        return button;
    }
//    class ButtonClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//
//        }
//    }
}
