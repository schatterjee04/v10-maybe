package com.example.rasam.origamihimitsu;

import android.Manifest;
import android.telephony.SmsManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Random;
import javax.crypto.*;
import javax.crypto.spec.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccessContact extends AppCompatActivity {
    Button searchContact;
    Button qRClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_contact);

        searchContact = (Button) findViewById(R.id.buttonSearch);
        qRClick= (Button)findViewById(R.id.btn_QRgen);


        searchContact.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int status = 0;
            if (ContextCompat.checkSelfPermission(AccessContact.this,
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(AccessContact.this,
                        Manifest.permission.READ_CONTACTS)) {
                } else {
                    ActivityCompat.requestPermissions(AccessContact.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            status);
                }
            }

            Intent contactsQuery = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            contactsQuery.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

            try{
                startActivityForResult(contactsQuery, 1);
                try {
                    onActivityResult(1, RESULT_OK, contactsQuery);
                }
                catch (Exception e){
                    //Snackbar.make(view, "Something went wrong...", Snackbar.LENGTH_LONG)
                            //.setAction("Action", null).show();
                }
                //Snackbar.make(view, "Long press detected. Loading contacts.", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
            }
            catch(ActivityNotFoundException e) {
                //Snackbar.make(view, "Contacts permissions not found. Failed to load contacts.", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
            }
            //return true;
        }
    });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                final String number = cursor.getString(column);

                Intent startMessaging = new Intent(AccessContact.this, messenger.class);
                startMessaging.putExtra("number", number);
                startActivity(startMessaging);
            }
        }
    }
}
