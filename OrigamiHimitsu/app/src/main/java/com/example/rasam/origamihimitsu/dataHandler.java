package com.example.rasam.origamihimitsu;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Sourav on 4/26/17.
 */

public class dataHandler extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "Messages";
    public static final String COLUMN_SENDER = "Sender";
    public static final String COLUMN_RECIPIENT = "Recipient";
    public static final String COLUMN_MESSAGE = "Message";
    public static final String COLUMN_TIME = "Timestamp";

    private static final String DATABASE_NAME = "Message_Log.db";


    private static final int DATABASE_VERSION = 2;

    // Template of creating a table in Android
    /*
    private static final String DICTIONARY_TABLE_NAME = "dictionary";
    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                    KEY_WORD + " TEXT, " +
                    KEY_DEFINITION + " TEXT);";
    */

    //create database
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + " ( " +
            BaseColumns._ID + " integer primary key autoincrement, " //need primary key
            + COLUMN_MESSAGE + " TEXT,"
            + COLUMN_RECIPIENT + "TEXT,"
            + COLUMN_SENDER + "TEXT,"
            + COLUMN_TIME + "DATETIME DEFAULT CURRENT_TIMESTAMP);";

    dataHandler(Context context) {
        super(context, "Message_Log", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(dataHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}


