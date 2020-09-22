package com.example.myapplication;

import android.annotation.SuppressLint;
import android.nfc.NdefRecord;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import androidx.core.util.Preconditions;

public class TextRecord implements ParsedRecord {
    private final String mLanguageCode;

    private final String mText;

    @SuppressLint("RestrictedApi")
    private TextRecord(String languageCode, String text) {
        mLanguageCode = Preconditions.checkNotNull(languageCode);
        mText = Preconditions.checkNotNull(text);
    }

    @Override
    public int getType() {
        return ParsedRecord.TYPE_TEXT;
    }

    public String getText() {
        return mText;
    }

    public String getLanguageCode() {
        return mLanguageCode;
    }

    @SuppressLint("RestrictedApi")
    public static TextRecord parse(NdefRecord record) {
        Preconditions.checkArgument(record.getTnf() == NdefRecord.TNF_WELL_KNOWN);
        Preconditions.checkArgument(Arrays.equals(record.getType(), NdefRecord.RTD_TEXT));
        try {
            byte[] payload = record.getPayload();

            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
            int languageCodeLength = payload[0] & 0077;
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            String text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            return new TextRecord(languageCode, text);
        } catch (UnsupportedEncodingException e) {
            // should never happen unless we get a malformed tag.
            throw new IllegalArgumentException(e);
        }
    }
    public static boolean isText(NdefRecord record) {
        try {
            parse(record);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}