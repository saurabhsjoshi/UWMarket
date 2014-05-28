package com.collegecode.api;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by saurabh on 5/25/14.
 * Manage All Databases use in the app
 * Support class for storing items and requests offline
 */


public class DataHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UWMDatabase";

    // Books table name
    private static final String TABLE_BOOKS = "books";

    // Clicker table name
    private static final String TABLE_CLICKER = "clicker";

    public DataHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_BOOKS_TABLE = "CREATE TABLE " + TABLE_BOOKS + "("
                + "objectid" + " TEXT PRIMARY KEY," + "title" + " TEXT,"
                + "author" + " TEXT,"
                + "isbn TEXT, price TEXT, fbid TEXT, condition TEXT, description TEXT, uploaded TEXT)";

        String CREATE_CLICKER_TABLE = "CREATE TABLE " + TABLE_CLICKER + "("
                + "objectid" + " TEXT PRIMARY KEY," + "title" + " TEXT,"
                + "price TEXT, fbid TEXT, description TEXT, uploaded TEXT)";

        sqLiteDatabase.execSQL(CREATE_BOOKS_TABLE);
        sqLiteDatabase.execSQL(CREATE_CLICKER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
