package com.example.myapplication;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.UserModel;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView welcome_textview;
    String uid;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.count_view);
        welcome_textview = findViewById(R.id.mainActivity_welcome_textview);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                welcome_textview.setText(userModel.userName+"님 환영합니다.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        })

//        Intent passedIntent = getIntent();
//        processIntent(passedIntent);

        Button assignButton = (Button) findViewById(R.id.assignButton);
        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());

                if(nfcAdapter == null) {
                    Toast.makeText(getApplicationContext(), "NFC 태그를 활성화 해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "NFC 태그를 스캔합니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), NFCRead.class);
                    startActivityForResult(intent, 101);
                }


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

//    private void processIntent(Intent intent) {
//        if(intent != null) {
//            String hour = intent.getStringExtra("hours");
//            String min = intent.getStringExtra("minutes");
//            textView.setText(hour + " : " + min);
//        } else {
//            textView.setText("이용 중인 좌석이 없습니다.");
//        }
//    }
}