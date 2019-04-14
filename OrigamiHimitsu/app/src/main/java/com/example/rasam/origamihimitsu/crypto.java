package com.example.rasam.origamihimitsu;

import javax.crypto.*;
import javax.crypto.spec.*;
import android.util.*;
import java.security.*;


/**
 * Created by rasam on 24/04/2017.
 */

public class crypto {

    public crypto(){}
    public static void myCrypto(){}

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
            Log.d("Decryption error:", e.toString());
        }
        return "error";
    }
}
