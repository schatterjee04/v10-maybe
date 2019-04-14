package com.example.rasam.origamihimitsu;

import android.app.Activity;
import android.nfc.*;
import android.os.Bundle;
import android.util.Log;

import static android.nfc.NdefRecord.createMime;


public class QRgenerate extends Activity implements NfcAdapter.CreateNdefMessageCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerate);

        NfcAdapter myNFCAdapter;

        try{
            myNFCAdapter = NfcAdapter.getDefaultAdapter(this);
        }
        catch(Exception e){
            Log.d("NFC Error:", e.toString());
        }
    }
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String text = ("Beam me up, Android!\n\n" +
                "Beam Time: " + System.currentTimeMillis());
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMime(
                        "application/vnd.com.example.android.beam", text.getBytes())
                        /**
                         * The Android Application Record (AAR) is commented out. When a device
                         * receives a push with an AAR in it, the application specified in the AAR
                         * is guaranteed to run. The AAR overrides the tag dispatch system.
                         * You can add it back in to guarantee that this
                         * activity starts when receiving a beamed message. For now, this code
                         * uses the tag dispatch system.
                        */
                        //,NdefRecord.createApplicationRecord("com.example.android.beam")
                });
        return msg;
    }
}
