package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CityDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE = "CityDatabase";
    public static final String TABLE = "Cities";
    public static final int VERSION = 3;
    public static final String ID = "id";
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String REGION = "region";
    public static final String CURRENCY = "currency_code";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    public CityDatabaseHelper(@Nullable Context context) { super(context, DATABASE, null, VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + "("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+CITY+" TEXT,"+COUNTRY+" TEXT,"+REGION+" TEXT,"+CURRENCY+" TEXT,"+LATITUDE+" TEXT,"+LONGITUDE+" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}
