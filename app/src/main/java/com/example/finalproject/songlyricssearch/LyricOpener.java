package com.example.finalproject.songlyricssearch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LyricOpener extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "searches.db";
    private static final int VERSION_NUMBER = 1;
    public static final String TABLE_NAME = "SEARCH";
    public static final String COL_ID = "_id";
    public static final String COL_ARTIST = "ARTIST";
    public static final String COL_SONG = "SONG";
    private static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME +
            " ("+COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_ARTIST + " text," + COL_SONG + " text)";

    public LyricOpener(Context cxt) {
        super(cxt,DATABASE_NAME,null,VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}