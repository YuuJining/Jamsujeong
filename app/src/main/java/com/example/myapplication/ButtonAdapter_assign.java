package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ButtonAdapter_assign extends BaseAdapter {
    Context context = null;
    String[] ButtonNames = null;
    private LayoutInflater thisInflater;

    public ButtonAdapter_assign(Context context, String[] Buttons) {
        this.context = context;
        this.thisInflater = LayoutInflater.from(context);
        ButtonNames = Buttons;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Button button = null;

        if(convertView != null) {
            button = (Button)convertView;
        } else {

            convertView = thisInflater.inflate(R.layout.grid_item, parent, false);
            ImageView button_default = (ImageView) convertView.findViewById(R.id.button_background);

            button = new Button(context);
            button.setText(ButtonNames[position]);
            button_default.setImageResource(R.drawable.button_design1);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int seatId = (int) getItemId(position) + 1;

//                    Toast.makeText(context,"좌석 " + getItemId(seatId) + "을 이용하겠습니까?", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("좌석 " + getItemId(seatId) + "을 이용하시겠습니까?");

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,"좌석 " + getItemId(seatId) + " 선택이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context, SetTimeActivity.class);
                                context.startActivity(intent);

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
