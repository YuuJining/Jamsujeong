package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.count_view);

        Intent passedIntent = getIntent();
        processIntent(passedIntent);

        Button assignButton = (Button) findViewById(R.id.assignButton);
        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AssignActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        Button checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CheckActivity.class);
                startActivityForResult(intent, 101);
            }
        });


    }

    private void processIntent(Intent intent) {
        if(intent != null) {
            String hour = intent.getStringExtra("hours");
            String min = intent.getStringExtra("minutes");
            textView.setText(hour + " : " + min);
        } else {
            textView.setText("이용 중인 좌석이 없습니다.");
        }
    }
}