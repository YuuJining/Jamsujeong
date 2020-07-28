//package com.example.myapplication;
//
//import android.app.AlertDialog;
//import android.app.AppComponentFactory;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.nfc.NdefMessage;
//import android.nfc.NdefRecord;
//import android.nfc.NfcAdapter;
//import android.nfc.Tag;
//import android.nfc.tech.Ndef;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.os.PatternMatcher;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import java.io.IOException;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class ReadNfcTag extends AppCompatActivity {
//    private NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//    private PendingIntent mPendingIntent;
//    private static final String TAG = "NFCReadTag";
//    private IntentFilter[] mNdefExchangeFilters;
//    private Context context;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.nfc_read);
//
//        context = getApplicationContext();
//
//        isNfcAvailable();
//        isNfcEnabled();
//
//        //Pending Intent 정의
//        mPendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//                | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
//        IntentFilter smartwhere = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        smartwhere.addDataScheme("http");
//        smartwhere.addDataAuthority("www.smartwhere.com", null);
//        smartwhere.addDataPath(".*", PatternMatcher.PATTERN_SIMPLE_GLOB);
//        mNdefExchangeFilters = new IntentFilter[] { smartwhere };
//    }
//
//    //     NFC설정 접근권한 확인
//    public void isNfcEnabled() {
//        if(!mNfcAdapter.isEnabled()) {
//            startActivity(new Intent("android.settings.NFC_SETTINGS"));
//        }
//    }
//    //     NFC태그 사용 가능한지 확인
//    public void isNfcAvailable() {
//        if (mNfcAdapter == null) {
//            Toast.makeText(getApplicationContext(), "NFC태그 사용이 가능한 단말이 아닙니다.", Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        }
//    public void ReadTagData(NdefMessage msg) {
//        if(msg == null) {
//            return;
//        }
//        String message = "";
//        message += msg.toString() + "\n";
//        NdefRecord[] records = msg.getRecords();
//
//        for(NdefRecord ndefRecord : records) {
//            byte[] payload = ndefRecord.getPayload();
//            String textEncoding = "UTF-8";
//
//            if(payload.length > 0) {
//                textEncoding = (payload[0] & 0200) == 0 ? "UTF-8" : "UTF-16";
//            }
//            Short tnf = ndefRecord.getTnf();
//            String type = String.valueOf(ndefRecord.getType());
//            String payloadStr = new String(ndefRecord.getPayload(), Charset.forName(textEncoding));
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(mNfcAdapter != null) {
//            mNfcAdapter.disableForegroundDispatch(this);
//        }
//    }
//    //
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
//            NdefMessage[] messages = null;
//            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//            if(rawMsgs != null) {
//                messages = new NdefMessage[rawMsgs.length];
//                for(int i = 0; i < rawMsgs.length; i++) {
//                    messages[i] = (NdefMessage) rawMsgs[i];
//                }
//            }
//            if(messages[0] != null) {
//                String result = "";
//                byte[] payload = messages[0].getRecords()[0].getPayload();
//                for(int j = 1; j < payload.length; j++) {
//                    result += (char) payload[j];
//                }
//                Toast.makeText(context, "Tag Contains " + result, Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
//
//    // NFC Intent 탐색 (인텐트 얻어오기)
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mNfcAdapter != null) {
//            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mNdefExchangeFilters, null);
//            if (!mNfcAdapter.isEnabled()) {
//                LayoutInflater inflater = getLayoutInflater();
//                View diaglayou = inflater.inflate(R.layout.nfc_setting, (ViewGroup) findViewById(R.id.nfc_setting));
//                new AlertDialog.Builder(this).setView(dialoglayout).setPositiveButton("Update Settings.", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent setnfc = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//                        startActivity(setnfc);
//                    }
//                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        finish();
//                    }
//                }).create().show();
//            }
//        } else {
//            Toast.makeText(context, "Sorry, No Nfc Adapter found.", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void GetDataFromTag(Tag tag, Intent intent) {
//        Ndef ndef = Ndef.get(tag);
//        try {
//            ndef.connect();
//
//            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//
//            if(messages != null) {
//                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
//                for(int i = 0; i < messages.length; i++) {
//                    ndefMessages[i] = (NdefMessage) messages[i];
//                }
//
//                NdefRecord record = ndefMessages[0].getRecords()[0];
//                byte[] payload = record.getPayload();
//                String text = new String(payload);
//                Log.e("TAG", "payload: " + text);
//                ndef.close();
//            }
//        } catch (IOException e) {
//            Toast.makeText(getApplicationContext(), "Cannot Read From Tag", Toast.LENGTH_LONG).show();
//        }
//    }
//}
