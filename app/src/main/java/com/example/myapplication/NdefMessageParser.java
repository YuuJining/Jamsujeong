package com.example.myapplication;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import java.util.ArrayList;
import java.util.List;

public class NdefMessageParser {
    private NdefMessageParser() {}

    public static List<ParsedRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }
    public static List<ParsedRecord> getRecords(NdefRecord[] records) {
        List<ParsedRecord> elements = new ArrayList<ParsedRecord>();
        for(NdefRecord record : records) {
            if(TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            }
        }
        return elements;
    }
}
