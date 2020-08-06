package com.example.finalproject;

import android.graphics.Bitmap;

/** This is a simple class for storing song data into an object
 *
 */

public class DeezerSong {
    private String title;
    private String album;
    private String artistName;
    private String coverURL;
    private Bitmap cover;
    private int duration;
    private long id;


    //constructor
    public DeezerSong(String title, String album, String artistName, String coverURL, Bitmap cover, int duration){
        //default id will be 123456
        //this id will not be used and should not cause primary key constraint issues in the database
        //as DeezerSong objects created using this constructor should not be saved to favourites and therefore not save to the database
        this(title, album, artistName, coverURL, cover, duration, 123456);
    }

    //constructor
    public DeezerSong(String title, String album, String artistName, String coverURL, Bitmap cover, int duration, long id){
        this.title = title;
        this.album = album;
        this.artistName = artistName;
        this.coverURL = coverURL;
        this.cover = cover;
        this.duration = duration;
        this.id = id;
    }

    //getter methods
    public String getTitle(){ return this.title;}
    public String getAlbum(){ return this.album;}
    public String getArtistName(){ return this.artistName;}
    public String getCoverURL(){ return this.coverURL;}
    public Bitmap getCover(){ return this.cover;}
    public int getDuration(){ return this.duration;}
    public long getId(){ return this.id;}

}
