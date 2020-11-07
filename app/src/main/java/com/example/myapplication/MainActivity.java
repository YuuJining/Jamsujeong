package com.example.myapplication;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.ReservationModel;
import model.SeatModel;
import model.UserModel;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView welcome_textview;
    TextView seatnumber_textview;
    String uid;
    String num;
    String seatNum;
    String cseatNum;
    String usingSeatNum;
    boolean userFlag;
    private NfcAdapter nfcAdapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference currentUser;
    private DatabaseReference reservation = FirebaseDatabase.getInstance().getReference().child("reservation");
    private Calendar alarmTime = Calendar.getInstance();
    UserModel currentU;
    ReservationModel currentR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUser = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        textView = findViewById(R.id.count_view);
        welcome_textview = findViewById(R.id.mainActivity_welcome_textview);
        seatnumber_textview = findViewById(R.id.mainActivity_seatnum_textview);
        //사용 중인 좌석 seatNum값 가져오기 & 사용자 이름 설정
        database.getInstance().getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                welcome_textview.setText(userModel.userName+"님 환영합니다.");
                num = Integer.toString(userModel.usingSeatNum);
                cseatNum = "seat" + num;
                userFlag = userModel.flag;

                if(Integer.parseInt(num) != 0) {
                    database.getInstance().getReference("seat").child(cseatNum).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            SeatModel seatModel = dataSnapshot.getValue(SeatModel.class);
                            Date reservationEndTime = new Date(seatModel.endTime);
                            SimpleDateFormat simpleDate = new SimpleDateFormat("hh시 mm분");
                            String endTime = simpleDate.format(reservationEndTime);
                            textView.setText(endTime+"까지 이용 가능합니다.");

                            if(Integer.parseInt(num) > 100 && Integer.parseInt(num) < 135) {
                                seatnumber_textview.setVisibility(View.VISIBLE);
                                int realNumber = Integer.parseInt(num)-100;
                                seatnumber_textview.setText("snapzone "+realNumber+"번 좌석");
                            }else if(Integer.parseInt(num) > 200 && Integer.parseInt(num) < 210) {
                                seatnumber_textview.setVisibility(View.VISIBLE);
                                int realNumber = Integer.parseInt(num)-200;
                                seatnumber_textview.setText("forest "+realNumber+"번 좌석");
                            }else {
                                seatnumber_textview.setVisibility(View.VISIBLE);
                                int realNumber = Integer.parseInt(num)-300;
                                seatnumber_textview.setText("library "+realNumber+"번 좌석");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    textView.setText("이용 중인 좌석이 없습니다.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //새로고침 누르면 시간 지난 reservation 삭제
        Button refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            long now = Calendar.getInstance().getTimeInMillis();
            @Override
            public void onClick(View v) {
                reservation.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            ReservationModel rmodel = ds.getValue(ReservationModel.class);
                            if((long) rmodel.endTime < now){
                                database.getInstance().getReference().child("users").child(rmodel.uid).child("flag").setValue(false);
                                database.getInstance().getReference().child("users").child(rmodel.uid).child("usingSeatNum").setValue(0);
                                seatNum = "seat" + ds.getKey();
                                database.getInstance().getReference().child("seat").child(seatNum).child("seatflag").setValue(false);
                                reservation.child(ds.getKey()).removeValue();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getApplicationContext(), "새로고침 완료", Toast.LENGTH_LONG).show();
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
                            database.getInstance().getReference().child("seat").child(seatNum).child("endTime").setValue(0);
                            database.getInstance().getReference().child("reservation").child(num).removeValue();
                            textView.setText("이용 중인 좌석이 없습니다.");
                        }
                        //로그아웃
                        else {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            Toast.makeText(getApplicationContext(), "로그아웃 선택됨", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            database.getInstance().getReference().child("users").child(uid).child("flag").setValue(false);
                            database.getInstance().getReference().child("users").child(uid).child("usingSeatNum").setValue(0);
                            database.getInstance().getReference().child("seat").child(seatNum).child("seatflag").setValue(false);
                            database.getInstance().getReference().child("reservation").child(num).removeValue();
                            startActivityForResult(intent, 200);

                        }
                    }
                }).create().show();
            }
        });

//        Intent passedIntent = getIntent();
//        processIntent(passedIntent);

        Button assignButton = (Button) findViewById(R.id.assignButton);
        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
                if(userFlag == true) {
                    Toast.makeText(getApplicationContext(), "먼저 이용 중인 좌석을 해제해주세요!", Toast.LENGTH_LONG).show();
                } else {
                    if (nfcAdapter == null) {
                        Toast.makeText(getApplicationContext(), "NFC 태그를 활성화 해주세요.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "NFC 태그를 스캔합니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), NFCRead.class);
                        startActivityForResult(intent, 102);
                    }
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

        //알람 구현
//        currentUser.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                currentU = dataSnapshot.getValue(UserModel.class);
//                if(currentU.flag == true) {
//                    reservation.child(String.valueOf(currentU.usingSeatNum)).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            currentR = dataSnapshot.getValue(ReservationModel.class);
//                            if (currentR.alert == true) setAlarm();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage("종료하시겠습니까?");

        //"예" 버튼을 누르면 실행되는 리스너
        alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });

        //"아니오" 버튼을 누르면 실행되는 리스너
        alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alBuilder.setTitle("프로그램 종료");
        alBuilder.show();
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

    private void setAlarm() {
        //알람 시간 설정
        alarmTime.setTimeInMillis(currentR.endTime);

        //Receiver 설정
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //알람 설정
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
    }



}