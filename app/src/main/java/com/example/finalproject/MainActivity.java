package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button deezerButton = (Button) findViewById(R.id.deezerSongSearchButton);
        deezerButton.setOnClickListener( v -> {
            Intent goToDeezer = new Intent(MainActivity.this, DeezerSearchMain.class);
            startActivity(goToDeezer);
        });


    }
}