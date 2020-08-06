package com.example.finalproject;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

//import com.example.FinalProject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = findViewById(R.id.x);
        b.setOnClickListener(v -> {
            Intent i = new Intent(this,LocationPicker.class);
            startActivity(i);
        });
    }
}