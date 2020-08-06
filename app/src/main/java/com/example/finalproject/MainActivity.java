package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.finalproject.songlyricssearch.LyricMainActivity;

//import com.example.finalproject.songlyricsearch.LyricMainActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
//        button.setOnClickListener((v) ->{
//            Intent intent = new Intent(MainActivity.this, GeoMain.class);
//        startActivity(intent);
//        });
//        Button button2 = findViewById(R.id.button2);
//        button2.setOnClickListener((v) -> {
//            Intent intent = new Intent(MainActivity.this, SoccerMatchMain.class);
//            startActivity(intent);
//        });
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        LyricMainActivity.class);
                startActivity(intent);
            }
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        DeezerSearchMain.class);
                startActivity(intent);
            }
        });
    }
}