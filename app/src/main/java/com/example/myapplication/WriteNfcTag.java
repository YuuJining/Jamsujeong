//package com.example.myapplication;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.nfc.NfcAdapter;
//import android.nfc.Tag;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//
//public class WriteNfcTag extends Activity {
//    private static final String TAG = "NFCWriteTag";
//    private NfcAdapter mNfcAdapter;
//    private IntentFilter[] mWriteTagFilters;
//    private PendingIntent mNfcPendingIntent;
//    private boolean silent = false;
//    private boolean writeProtect = false;
//    private Context context;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = getApplicationContext();
//
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//                | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
//        IntentFilter discovery = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
//        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
//
//        mWriteTagFilters = new IntentFilter[] { discovery };
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(!mNfcAdapter != null) {
//            if (!mNfcAdapter.isEnabled()) {
//                LayoutInflater inflater = getLayoutInflater();
//                View dialoglayout = inflater.inflate(R.layout.nfc_settings_layout, (ViewGroup) findViewById(R.id.nfc_settings_layout));
//                new AlertDialog.Builder(this).setView(dialoglayout)
//                        .setPositiveButton("Update Settings.", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent setnfc = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//                                startActivity(setnfc);
//                            }
//                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        finish();
//                    }
//                }).create().show();
//            }
//            mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
//        } else {
//            Toast.makeText(context, "Sorry, No NFC Adapter found.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(mNfcAdapter != null) mNfcAdapter.disableForegroundDispatch(this);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
//
//            // validiate that this tag can be written
//            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//
//            if(supportedTechs(detectedTag.getTechList())) {
//                //check if tag is writablel to the extent that we can
//                if(writableTag(detectedTag)) {
//                    //writeTag here
//                    WriteResponse wr = writeTag(getTaskId(), detectedTag);
//                    String message = (wr.getStatus() == 1)
//                }
//            }
//        }
//    }
//}
