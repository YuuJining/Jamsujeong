package com.example.myapplication;

import android.content.Context;
import android.view.View;

public class ButtonClickListener implements View.OnClickListener {

    protected Context context = null;

    public ButtonClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }
}
