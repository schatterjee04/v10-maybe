package com.example.rasam.origamihimitsu;

import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.telephony.*;
import android.widget.Toast;

import static com.example.rasam.origamihimitsu.dataHandler.COLUMN_MESSAGE;
import static com.example.rasam.origamihimitsu.dataHandler.COLUMN_RECIPIENT;
import static com.example.rasam.origamihimitsu.dataHandler.COLUMN_TIME;
import static com.example.rasam.origamihimitsu.dataHandler.TABLE_NAME;

/**
 * Created by Sourav on 4/26/17.
 */

public class smsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
    //—get the SMS message passed in—
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String messages = "";
        if (bundle != null)
        {
    //—retrieve the SMS message received—
            Object[] smsExtra = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[smsExtra.length];

            for (int i=0; i<msgs.length; i++)
            {
                SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
                //take out content from sms
                String body = sms.getMessageBody().toString();
                String address = sms.getOriginatingAddress();

                messages += "SMS from " + address + " :\n";
                messages += body + "\n";

                putSmsToDatabase(sms, context );
            }
            //—display the new SMS message—

            Toast.makeText(context, messages, Toast.LENGTH_SHORT).show();
        }
    }

    private void putSmsToDatabase( SmsMessage sms, Context context )
    {
        dataHandler dataBaseHelper = new dataHandler(context);

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime(    ));
        // Create SMS row
        ContentValues values = new ContentValues();

        values.put(COLUMN_RECIPIENT, sms.getOriginatingAddress().toString() );
        values.put(COLUMN_TIME, mydate);
        values.put(COLUMN_MESSAGE, sms.getMessageBody().toString());
        // values.put( READ, MESSAGE_IS_NOT_READ );
        // values.put( STATUS, sms.getStatus() );
        // values.put( TYPE, MESSAGE_TYPE_INBOX );
        // values.put( SEEN, MESSAGE_IS_NOT_SEEN );

        db.insert(TABLE_NAME , null, values);

        db.close();

    }
}

