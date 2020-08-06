package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LocationPicker extends AppCompatActivity {

    EditText lat, lng;
    SharedPreferences preferences;
    Button go,saved;
    String lati,lngi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        lat=findViewById(R.id.lat_picker);
        lng=findViewById(R.id.lng_picker);
        go=findViewById(R.id.go_button);
        saved=findViewById(R.id.saved_cities);

        preferences = getApplicationContext().getSharedPreferences("prefID", 0);
        lati = preferences.getString("lat","");
        lngi = preferences.getString("lng","");
        lat.setText(lati);
        lng.setText(lngi);

        go.setOnClickListener(v -> {
            Intent nextActivity = new Intent(this,CitySearch.class);
            nextActivity.putExtra("lat",lat.getText().toString());
            nextActivity.putExtra("lng",lng.getText().toString());
            Toast.makeText(getApplicationContext(),"Nearby Cities found", Toast.LENGTH_SHORT).show();
            startActivity(nextActivity);
        });

        saved.setOnClickListener(v -> {
            Intent savedActivity = new Intent(this,SavedCities.class);
            startActivity(savedActivity);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText lat = findViewById(R.id.lat_picker);
        EditText lng = findViewById(R.id.lng_picker);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("prefID",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lat",lat.getText().toString());
        editor.putString("lng",lng.getText().toString());
        editor.apply();
    }


}
