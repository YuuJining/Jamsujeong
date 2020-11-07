package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AssignActivity extends AppCompatActivity {

    Fragment_forest_assign forrest;
    Fragment_library_assign library;
    Fragment_snapzone_assign snapzone;
    Fragment_library_check check;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);

        FragmentManager manager = getSupportFragmentManager();

        Intent passedIntent = getIntent();

        textView = (TextView) findViewById(R.id.fragment_title);
        textView.setText(passedIntent.getStringExtra("nfcText"));

        manager.beginTransaction().add(R.id.container, setFragment(passedIntent)).commit();

    }

    public Fragment setFragment(Intent intent) {
        String text = intent.getStringExtra("nfcText");
        if(text.equals("수정관수면실")) {
            snapzone = new Fragment_snapzone_assign();
            return snapzone;

        } else if(text.equals("운정캠도서관")) {
            library = new Fragment_library_assign();
            return library;

        } else if(text.equals("난향관Forrest")) {
            forrest = new Fragment_forest_assign();
            return forrest;

        } else {
            Toast.makeText(getApplicationContext(), "잘못된 전달입니다.", Toast.LENGTH_SHORT).show();
        }
        return check;
    }
}
