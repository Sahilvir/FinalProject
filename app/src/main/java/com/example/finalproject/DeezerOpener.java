package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DeezerOpener extends SQLiteOpenHelper {

    //database data
    protected final static String DATABASE_NAME = "DeezerFavsDB";
    protected final static int VERSION_NUM = 1;

    //table data
    public final static String TABLE_NAME = "FAVOURITES";
    public final static String COL_ARTIST = "ARTIST";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_ALBUM = "ALBUM";
    public final static String COL_DUR = "DURATION";
    public final static String COL_COVER = "COVER";
    public final static String COL_ID = "_id";

    //constructor
    public DeezerOpener(Context ctx) { super(ctx, DATABASE_NAME, null, VERSION_NUM);}

    /**Function gets called if no database file exists
     *
     * @param db - database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creates a table with auto-incrementing id as the primary key
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ARTIST + " text,"
                + COL_TITLE + " text,"
                + COL_ALBUM + " text,"
                + COL_DUR + " INTEGER,"
                + COL_COVER + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
