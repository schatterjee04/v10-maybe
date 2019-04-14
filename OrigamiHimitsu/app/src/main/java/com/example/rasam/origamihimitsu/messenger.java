package com.example.rasam.origamihimitsu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.support.v7.app.*;
import android.telephony.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class messenger extends AppCompatActivity {
    String incomingMessagesLog;
    boolean messageLogHasChanged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        final String contactNumber = getIntent().getExtras().getString("number");

        final TextView mySecureMessage = (TextView) findViewById(R.id.editInput);
        final MultiAutoCompleteTextView messageHistory = (MultiAutoCompleteTextView)findViewById(R.id.messageContent);
        final String welcomeString = "Welcome to the secure message utility! \n\nYou are messaging: " + contactNumber + "\n";

        messageHistory.setText(welcomeString);
        if (ContextCompat.checkSelfPermission(messenger.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(messenger.this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(messenger.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        0);
            }
        }

        if (ContextCompat.checkSelfPermission(messenger.this,
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(messenger.this,
                    Manifest.permission.RECEIVE_SMS)) {
            } else {
                ActivityCompat.requestPermissions(messenger.this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        0);
            }
        }
       //FAB button code:
        //First reveal fab buttons
        FloatingActionButton sendMessage = (FloatingActionButton) findViewById(R.id.selectMesenger);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SMS button visibility
                FloatingActionButton sendSecureMessage = (FloatingActionButton) findViewById(R.id.sendSMS);
                sendSecureMessage.setVisibility(View.VISIBLE);
                //FBMsg button visibility
                FloatingActionButton sendSecureFBMessage = (FloatingActionButton) findViewById(R.id.sendFBmsg);
                sendSecureFBMessage.setVisibility(View.VISIBLE);
                //Whatsapp button visibility
                FloatingActionButton sendSecureWhatsApp = (FloatingActionButton) findViewById(R.id.sendWhatsapp);
                sendSecureWhatsApp.setVisibility(View.VISIBLE);

                // Sending Secure SMS
                sendSecureMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String messagePlainText = mySecureMessage.getText().toString();
                        String cypherText;
                        String plainTextDecrypted;
                        int status = 0;
                        if (ContextCompat.checkSelfPermission(messenger.this,
                                Manifest.permission.SEND_SMS)
                                != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(messenger.this,
                                    Manifest.permission.SEND_SMS)) {
                            } else {
                                ActivityCompat.requestPermissions(messenger.this,
                                        new String[]{Manifest.permission.SEND_SMS},
                                        status);
                            }
                        }
                        SecureRandom randomSalt = new SecureRandom();
                        int salt = randomSalt.nextInt();
                        try {
                            SmsManager sendMessage = SmsManager.getDefault();
                            SecretKey key;
                            key = keyGen(makeKey());
                            messagePlainText = messagePlainText + salt;
                            byte[] encryptedMessage = encryptMyMessage(messagePlainText, key);
                            try {
                                cypherText = Base64.encodeToString(encryptedMessage, 0);
                                sendMessage.sendTextMessage(contactNumber, null, cypherText, null, null);
                                String appendMessage = "\nMessage plaintext: " + messagePlainText + "\nMessage ciphertext: " + cypherText;
                                String tmp = messageHistory.getText().toString();
                                messageHistory.setText(tmp + appendMessage);
                            } catch (Exception e) {
                                Snackbar.make(view, e.toString(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                String appendMessage = "\nError: " + e.toString();
                                String tmp = messageHistory.getText().toString();
                                messageHistory.setText(tmp + appendMessage);
                            }
                            try {
                                plainTextDecrypted = decryptMyMessage(encryptedMessage, key);
                                String tmp = messageHistory.getText().toString();
                                String appendMessage = "\nDecrypted message: " + plainTextDecrypted;
                                messageHistory.setText(tmp + appendMessage);
                            } catch (Exception e) {
                                Snackbar.make(view, e.toString(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                String appendMessage = "\nError: " + e.toString();
                                String tmp = messageHistory.getText().toString();
                                messageHistory.setText(tmp + appendMessage);
                            }
                        } catch (SecurityException e) {
                            Snackbar.make(view, "SMS permissions not granted, permission request prompt failed. Please grant permission manually in settings.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            String appendMessage = "\nError: " + e.toString();
                            String tmp = messageHistory.getText().toString();
                            messageHistory.setText(tmp + appendMessage);
                        }
                        mySecureMessage.setText("");
                        if (messageLogHasChanged == true) {
                            MultiAutoCompleteTextView messageHistory = (MultiAutoCompleteTextView) findViewById(R.id.messageContent);

                            String tmp = messageHistory.getText().toString();
                            String appendMessage = "\nReceived messages: " + incomingMessagesLog;
                            messageHistory.setText(tmp + appendMessage);
                            messageLogHasChanged = false;
                        }
                /*
                Snackbar.make(view, "Sent.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */


                    }


                });
                //Send a message  using FB Messenger
                sendSecureFBMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String messagePlainText = mySecureMessage.getText().toString();
                        String cypherText;
                        String plainTextDecrypted;
                        SecureRandom randomSalt = new SecureRandom();
                        int salt = randomSalt.nextInt();
                        try {

                            SecretKey key;
                            key = keyGen(makeKey());
                            messagePlainText = messagePlainText + salt;
                            byte[] encryptedMessage = encryptMyMessage(messagePlainText, key);
                            try {
                                cypherText = Base64.encodeToString(encryptedMessage, 0);
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent
                                        .putExtra(Intent.EXTRA_TEXT,
                                                cypherText);
                                sendIntent.setType("text/plain");
                                sendIntent.setPackage("com.facebook.orca");
                                    try {
                                    startActivity(sendIntent);
                                }
                                catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(messenger.this,"Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
                                }

                                String appendMessage = "\nMessage plaintext: " + messagePlainText + "\nMessage ciphertext: " + cypherText;
                                String tmp = messageHistory.getText().toString();
                                messageHistory.setText(tmp + appendMessage);
                            } catch (Exception e) {
                                Snackbar.make(view, e.toString(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                String appendMessage = "\nError: " + e.toString();
                                String tmp = messageHistory.getText().toString();
                                messageHistory.setText(tmp + appendMessage);
                            }
                            try {
                                plainTextDecrypted = decryptMyMessage(encryptedMessage, key);
                                String tmp = messageHistory.getText().toString();
                                String appendMessage = "\nDecrypted message: " + plainTextDecrypted;
                                messageHistory.setText(tmp + appendMessage);
                            } catch (Exception e) {
                                Snackbar.make(view, e.toString(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                String appendMessage = "\nError: " + e.toString();
                                String tmp = messageHistory.getText().toString();
                                messageHistory.setText(tmp + appendMessage);
                            }
                        } catch (SecurityException e) {
                            Snackbar.make(view, "SMS permissions not granted, permission request prompt failed. Please grant permission manually in settings.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            String appendMessage = "\nError: " + e.toString();
                            String tmp = messageHistory.getText().toString();
                            messageHistory.setText(tmp + appendMessage);
                        }
                        mySecureMessage.setText("");
                        if (messageLogHasChanged == true) {
                            MultiAutoCompleteTextView messageHistory = (MultiAutoCompleteTextView) findViewById(R.id.messageContent);

                            String tmp = messageHistory.getText().toString();
                            String appendMessage = "\nReceived messages: " + incomingMessagesLog;
                            messageHistory.setText(tmp + appendMessage);
                            messageLogHasChanged = false;
                        }



                    }
                } );
            sendSecureWhatsApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String messagePlainText = mySecureMessage.getText().toString();
                        String cypherText;
                        String plainTextDecrypted;
                        SecureRandom randomSalt = new SecureRandom();
                        int salt = randomSalt.nextInt();
                        try {

                            SecretKey key;
                            key = keyGen(makeKey());
                            messagePlainText = messagePlainText + salt;
                            byte[] encryptedMessage = encryptMyMessage(messagePlainText, key);
                            try {
                                cypherText = Base64.encodeToString(encryptedMessage, 0);
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent
                                        .putExtra(Intent.EXTRA_TEXT,
                                                cypherText);

                                sendIntent.setType("text/plain");
                                sendIntent.setPackage("com.whatsapp");
                                    try {
                                    startActivity(sendIntent);
                                }
                                catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(messenger.this,"Please Install WhatsApp", Toast.LENGTH_LONG).show();
                                }

                                String appendMessage = "\nMessage plaintext: " + messagePlainText + "\nMessage ciphertext: " + cypherText;
                                String tmp = messageHistory.getText().toString();
                                messageHistory.setText(tmp + appendMessage);
                            } catch (Exception e) {
                                Snackbar.make(view, e.toString(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                String appendMessage = "\nError: " + e.toString();
                                String tmp = messageHistory.getText().toString();
                                messageHistory.setText(tmp + appendMessage);
                            }
                            try {
                                plainTextDecrypted = decryptMyMessage(encryptedMessage, key);
                                String tmp = messageHistory.getText().toString();
                                String appendMessage = "\nDecrypted message: " + plainTextDecrypted;
                                messageHistory.setText(tmp + appendMessage);
                            } catch (Exception e) {
                                Snackbar.make(view, e.toString(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                String appendMessage = "\nError: " + e.toString();
                                String tmp = messageHistory.getText().toString();
                                messageHistory.setText(tmp + appendMessage);
                            }
                        } catch (SecurityException e) {
                            Snackbar.make(view, "SMS permissions not granted, permission request prompt failed. Please grant permission manually in settings.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            String appendMessage = "\nError: " + e.toString();
                            String tmp = messageHistory.getText().toString();
                            messageHistory.setText(tmp + appendMessage);
                        }
                        mySecureMessage.setText("");
                        if (messageLogHasChanged == true) {
                            MultiAutoCompleteTextView messageHistory = (MultiAutoCompleteTextView) findViewById(R.id.messageContent);

                            String tmp = messageHistory.getText().toString();
                            String appendMessage = "\nReceived messages: " + incomingMessagesLog;
                            messageHistory.setText(tmp + appendMessage);
                            messageLogHasChanged = false;
                        }



                    }
                } );




            }
        });}
    public static void myCrypto(){

    }

    public byte[] makeKey(){
        SecureRandom randomKeyValue = new SecureRandom();
        byte byteArray[] = new byte[16];
        randomKeyValue.nextBytes(byteArray);
        return byteArray;
    }

    public SecretKey keyGen(byte[] inputKey){
        SecretKey myTempKey = new SecretKeySpec(inputKey, "HmacSHA512");
        return myTempKey;
    }

    public byte[] encryptMyMessage(String myInputMessage, SecretKey myCryptoKey){
        byte[] myCipherText = null;
        try{
            Cipher myEncryptionCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            myEncryptionCipher.init(Cipher.ENCRYPT_MODE, myCryptoKey);
            myCipherText = myEncryptionCipher.doFinal(myInputMessage.getBytes("UTF-8"));
        }
        catch (Exception e){
            Log.d("Encryption error:", e.toString());
        }
        return myCipherText;
    }

    public String decryptMyMessage(byte[] myInputMessage, SecretKey myCryptoKey){
        String myPlainText;
        try{
            Cipher myEncryptionCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            myEncryptionCipher.init(Cipher.DECRYPT_MODE, myCryptoKey);
            myPlainText = new String(myEncryptionCipher.doFinal(myInputMessage), "UTF-8");
            return myPlainText;
        }
        catch (Exception e){
            Log.d("Error:", e.toString());
        }
        return "error";
    }
    public void inboundText(String incomingMessage){
        incomingMessagesLog += incomingMessage;
        messageLogHasChanged = true;
    }
}