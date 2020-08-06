package com.example.finalproject;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class Details extends AppCompatActivity {

    TextView cityT,countryT,regionT,currencyT,latT,longT;
    String city,country,region,currency,latitude,longitude;
    Button maps,save;

    AlertDialog.Builder alert;
    SQLiteDatabase db;
    ContentValues values;
    CityDatabaseHelper helper;
    Snackbar snack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        cityT = findViewById(R.id.city);
        countryT = findViewById(R.id.country);
        regionT = findViewById(R.id.region);
        currencyT = findViewById(R.id.currency);
        latT = findViewById(R.id.latitude);
        longT = findViewById(R.id.longitude);

        city =getIntent().getStringExtra("city");
        country =getIntent().getStringExtra("country");
        currency =getIntent().getStringExtra("currency");
        region =getIntent().getStringExtra("region");
        latitude =getIntent().getStringExtra("latitude");
        longitude =getIntent().getStringExtra("longitude");

        cityT.setText("City: "+ city);
        countryT.setText("Country: "+ country);
        currencyT.setText("Currency: "+ currency);
        regionT.setText("Region: "+ region);
        latT.setText("Latitude: "+ latitude);
        longT.setText("Longitude: "+ longitude);

        maps=findViewById(R.id.maps);
        maps.setOnClickListener(v -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/@" +latitude+","+longitude+",15z"));
            startActivity(intent);

        });

        save=findViewById(R.id.save);
        save.setOnClickListener( v -> {
            alert = new AlertDialog.Builder(this);
            helper = new CityDatabaseHelper(this);
            db = helper.getWritableDatabase();
            values = new ContentValues();

            alert.setTitle("Save City");
            alert.setMessage("Do you want to save this city's details?");
            alert.setPositiveButton("Yes", (DialogInterface dialog, int which) -> {
                values.put(helper.CITY,city);
                values.put(helper.COUNTRY,country);
                values.put(helper.REGION,region);
                values.put(helper.CURRENCY,currency);
                values.put(helper.LATITUDE,latitude);
                values.put(helper.LONGITUDE,longitude);
                db.insert(helper.TABLE,null,values);
                snack.make(save,"City has been saved",Snackbar.LENGTH_SHORT).show();
            });
            alert.setNegativeButton("No",(DialogInterface dialog,int which) -> {

            });
            alert.show();
        });
    }
}
