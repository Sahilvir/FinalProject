package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SoccerDatabase extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "FinalDemo";
    public final static String TABLE_NAME = "SoccerMatch";
    public final static String COL_TITLE = "title";
    public final static String COL_DATE = "date";
    public final static String COL_URL = "url";
    public static final String COL_ID = "id";
    public final static String CREATE = "CREATE TABLE "+TABLE_NAME+"("+COL_ID+" INTEGER primary key autoincrement, "+COL_DATE+" text, "+COL_TITLE+" text, "+COL_URL+" text );";
    public static final int  DATABASE_VERSION=1;
    public final static String[] ALL_COLS = new String[] {COL_ID, COL_DATE, COL_TITLE, COL_URL};
    SQLiteDatabase db;

    public SoccerDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }

    public List<SoccerObject> getDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        List<SoccerObject> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, ALL_COLS,null, null, null, null, null);

        while (cursor.moveToNext()) {
            final String title=cursor.getString(cursor.getColumnIndex(COL_TITLE));
            final String date=cursor.getString(cursor.getColumnIndex(COL_DATE));
            final String url=cursor.getString(cursor.getColumnIndex(COL_URL));
            final long id=cursor.getLong(cursor.getColumnIndex(COL_ID));
            list.add(new SoccerObject(id,title,date,url));
        }
        cursor.close();
        return list;
    }

    public long insertWord(String title,String date,String url) {

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE,title);
        contentValues.put(COL_DATE,date);
        contentValues.put(COL_URL,url);
        return db.insert(TABLE_NAME,null,contentValues);

    }

    public void deleteWord(long id){
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+COL_ID+" = '"+id+"';");
    }


}
