package com.example.rasam.origamihimitsu;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import static java.lang.System.in;


public class LoginActivity extends AppCompatActivity  {

    EditText pinInput;
    Button  btn_SignIn, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_SignIn = (Button)findViewById(R.id.btn_SignIn);
        register = (Button)findViewById(R.id.createPIN);


        pinInput = (EditText)findViewById(R.id.pinInput);
        final String filename = "PinInput_file";

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View map_view) {
                int pin_string = Integer.parseInt(pinInput.getText().toString());
                FileInputStream inputStream;
                int readPIN;
                boolean valid = false;
                try{
                    inputStream = openFileInput(filename);
                    Scanner scan = new Scanner(inputStream);
                    while(scan.hasNext()) {
                        readPIN = Integer.parseInt(scan.nextLine());
                        if(pin_string == readPIN){
                            valid = true;
                            break;
                        }
                    }
                    if(!valid) {
                        Snackbar.make(map_view, "Pin does not exist. Please create a new pin.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else{
                        Intent launchMain = new Intent(LoginActivity.this, AccessContact.class);
                        startActivity(launchMain);
                    }
                    pinInput.setText("");
                }catch(Exception e){
                    Snackbar.make(map_view,e.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View map_view) {
                FileInputStream inputStream;
                FileOutputStream outputStream;

                boolean pinFound = false;
                int pin_string = Integer.parseInt(pinInput.getText().toString());
                int readPIN;

                try{
                    inputStream = openFileInput(filename);
                    Scanner scan = new Scanner(inputStream);
                    while(scan.hasNext()) {
                        readPIN = Integer.parseInt(scan.nextLine());
                        if(pin_string == readPIN){
                            pinFound = true;
                            break;
                        }
                    }
                    if(!pinFound){
                        try {
                            outputStream = openFileOutput(filename, MODE_APPEND);
                            outputStream.write(pin_string);
                            outputStream.close();
                            String temp = ""+pin_string;
                            Log.d("PIN written:", temp);

                        }
                        catch (Exception g){
                            Log.d("PIN not found:", g.toString());
                        }
                    }
                }
                catch (Exception e){
                    try {
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(pin_string);
                        outputStream.close();
                    } catch (Exception f) {
                        Log.d("Write file not found:", f.toString());
                    }
                }
                Snackbar.make(map_view, "Pin saved. Re enter pin and  submit." + pin_string, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                pinInput.setText("");

                
            }
        });
    }
}
        // Set up the login form.
        // Add a button to create a new pin.


