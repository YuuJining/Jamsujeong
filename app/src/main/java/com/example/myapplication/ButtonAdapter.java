package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ButtonAdapter extends BaseAdapter {
    Context context = null;
    String[] ButtonNames = null;

    public ButtonAdapter(Context context, String[] Buttons) {
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button = null;

        if(convertView != null) {
            button = (Button)convertView;
        } else {

            button = new Button(context);
            button.setText(ButtonNames[position]);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        return button;
    }
    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
}
