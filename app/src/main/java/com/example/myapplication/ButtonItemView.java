package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

public class ButtonItemView extends FrameLayout {

    public ButtonItemView(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.grid_item, this);
    }

    public void display() {

    }
}
