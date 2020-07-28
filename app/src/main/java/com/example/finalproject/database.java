package com.example.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "FavCityDB";
    protected final static int VERSION_NUM = 2;
    public final static String TABLE_NAME = "City";
    public final static String COL_MESSAGE = "MESSAGE";
    public final static String COL_TYPE = "TYPE";
    public final static String _ID = "_id";


    public database(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("+_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TYPE + " text,"+ COL_MESSAGE  + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }





}