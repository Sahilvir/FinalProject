package com.example.finalproject;


public class Cities {
    long id;
    String country,region,city,currency,lat,lng;

    public Cities(long id,String country,String region,String city,String currency,String lat,String lng)
    {
        this.id=id;
        this.country=country;
        this.region=region;
        this.city=city;
        this.currency=currency;
        this.lat=lat;
        this.lng=lng;
    }

    public long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getRegion() {
        return region;
    }
}
