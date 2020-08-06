package com.example.finalproject.songlyricssearch;


public class LyricSearch {

    private long Id;
    private String artistName;
    private String songName;
    public long getId() {
        return this.Id;
    }
    public LyricSearch(long Id, String artistName, String songName) {
        setId(Id);
        setArtistName(artistName);
        setSongName(songName);
    }


    private void setId(long Id) {
        this.Id = Id;
    }
    public String getArtistName() {
        return artistName;
    }
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
    public String getSongName() {
        return songName;
    }
    public void setSongName(String songName) {
        this.songName = songName;
    }

}
