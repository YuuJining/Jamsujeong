package com.example.myapplication;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NFCRead extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_read);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(nfcAdapter == null) {
            Toast.makeText(this, "NFC 태그를 활성화 해주세요.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "NFC 태그를 스캔 중입니다.", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(getApplicationContext(), AssignActivity.class);
    }
}
