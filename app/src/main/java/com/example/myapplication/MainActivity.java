package com.example.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.ReservationModel;
import model.UserModel;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView welcome_textview;
    TextView seatnumber_textview;
    String uid;
    String num;
    String seatNum;
    private NfcAdapter nfcAdapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //사용 중인 좌석 seatNum값 가져오기
        database.getInstance().getReference("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                num = Integer.toString(userModel.usingSeatNum);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //로그아웃/이용해제 버튼 동작 구현
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        final CharSequence[] items = {"좌석 이용 해제", "로그아웃"};
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("원하시는 동작을 선택해주세요.\n(로그아웃시 좌석이용 내역도 함께 해제됩니다.)");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        seatNum = "seat" + num;
                        //좌석이용 해제
                        if(items[index] == "좌석 이용 해제") {
                            Toast.makeText(getApplicationContext(), "좌석 이용 해제 선택됨", Toast.LENGTH_LONG).show();
                            database.getInstance().getReference().child("users").child(uid).child("flag").setValue(false);
                            database.getInstance().getReference().child("users").child(uid).child("usingSeatNum").setValue(0);
                            database.getInstance().getReference().child("seat").child(seatNum).child("seatflag").setValue(false);
                            database.getInstance().getReference().child("reservation").child(num).removeValue();
                        }
                        //로그아웃
                        else {
                            Toast.makeText(getApplicationContext(), "로그아웃 선택됨", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            database.getInstance().getReference().child("users").child(uid).child("flag").setValue(false);
                            database.getInstance().getReference().child("users").child(uid).child("usingSeatNum").setValue(0);
                            database.getInstance().getReference().child("seat").child(seatNum).child("seatflag").setValue(false);
                            database.getInstance().getReference().child("reservation").child(num).removeValue();
                        }
                    }
                }).create().show();
            }
        });

        textView = findViewById(R.id.count_view);
        welcome_textview = findViewById(R.id.mainActivity_welcome_textview);

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


//        FirebaseDatabase.getInstance().getReference("reservation").child().addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ReservationModel reservationModel = dataSnapshot.getValue(ReservationModel.class);
//                textView.setText(reservationModel.endTime+"\n까지 예약 중 입니다.");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



        Intent passedIntent = getIntent();
        processIntent(passedIntent);

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
                    startActivityForResult(intent, 102);
                }


            }
        });

        Button checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CheckActivity.class);
                startActivityForResult(intent, 103);
            }
        });

    }

    private void processIntent(Intent intent) {
        if(intent != null) {
            String hour = intent.getStringExtra("hours");
            String min = intent.getStringExtra("minutes");


            textView.setText("");
        } else {
            textView.setText("이용 중인 좌석이 없습니다.");
        }
    }
}