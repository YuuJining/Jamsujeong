package com.example.myapplication;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NFCRead extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    Intent targetIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private String NfcText;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_read);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        if(nfcAdapter == null) {
            Toast.makeText(this, "NFC태그를 활성화하세요", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NFC태그를 스캔하세요", Toast.LENGTH_SHORT).show();
        }

        // 인텐트 객체 생성
        targetIntent = new Intent(this, getClass());
        targetIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 102, targetIntent, 0);

        //인텐트 필터 객체 생성
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch(Exception e) {
            throw new RuntimeException("fail", e);
        }

        mFilters = new IntentFilter[] {ndef,};
        mTechLists = new String[][] { new String[] {NfcF.class.getName()} };

        Intent passedIntent = getIntent();
        if(passedIntent != null) {
            String action = passedIntent.getAction();
            if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
                processTag(passedIntent);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }
    /* NFC태그 스캔시 자동 호출되는 메소드
      스캔된 태그의 모든 정보 Intent에 저장되어 있음*/
    public void onNewIntent(Intent passedIntent) {
        super.onNewIntent(passedIntent);
        if(passedIntent != null) {
            processTag(passedIntent);
        }
    }

    //전달받은 intent에서 NFC태그에 등록한 정보 얻는 메소드
    private  void processTag(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(rawMsgs == null) {
            return;
        }
        Toast.makeText(getApplicationContext(), "스캔 성공", Toast.LENGTH_SHORT).show();

        Log.i("info", "rawMsgs.length: " + rawMsgs.length); //스캔한 태그 개수
        NdefMessage[] msgs;
        if(rawMsgs != null) {
            msgs = new NdefMessage[rawMsgs.length];
            for(int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
                NfcText = getTagData(msgs[i]);
            }
        }
        if(NfcText.equals("수정관수면실") || NfcText.equals("난향관Forrest") || NfcText.equals("운정캠도서관")) {
            Toast.makeText(this, "수면실 인증을 성공하였습니다", Toast.LENGTH_LONG).show();
            Intent assignIntent = new Intent(this, AssignAcitivity.class);
            assignIntent.putExtra("nfcText", NfcText);
            startActivityForResult(assignIntent, 105);
            NFCRead.this.finish();
        } else {
            Toast.makeText(this, "수면실 인증을 실패하였습니다", Toast.LENGTH_LONG).show();
//            // 일단 테스팅 위한 임시코드
//            Intent assignIntent = new Intent(this, AssignActivity.class);
//            startActivity(assignIntent);
        }
    }
    //NdefMessage에서 NdefRecord들을 추출 후 NdefRecord 내용 중 텍스트값만 추출
    private String getTagData(NdefMessage mMessage) {
        List<ParsedRecord> records = NdefMessageParser.parse(mMessage);
        final int size = records.size();
        String recordStr = "";

        for(int i = 0; i < size; i++) {
            ParsedRecord record = records.get(i);
            int recordType = record.getType();

            if(recordType == ParsedRecord.TYPE_TEXT) {
                recordStr = ((TextRecord) record).getText();
            } else {
                recordStr = "";
            }
        }
        return recordStr;
    }
}
