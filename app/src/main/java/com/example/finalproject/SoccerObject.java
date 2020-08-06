package com.example.finalproject;

public class SoccerObject {

    public long id;
    public String matchTitle;
    public String date;
    public String url;

    public SoccerObject(long id, String matchTitle, String date, String url) {
        this.id = id;
        this.matchTitle = matchTitle;
        this.date = date;
        this.url = url;
    }

    public SoccerObject(String matchTitle, String date, String url) {
        this.matchTitle = matchTitle;
        this.date = date;
        this.url = url;
    }

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}

    public long getId() {return id;}

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMatchTitle() {
        return matchTitle;
    }

    public void setMatchTitle(String matchTitle) {
        this.matchTitle = matchTitle;
    }


}
