package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class SoccerMatchDetail extends AppCompatActivity {

    private TextView matchDetail;
    private Button highlightButton;
    private Button saveButton;
    private SoccerDatabase soccerDatabase;
    private SoccerObject soccerObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_match_detail);
        soccerDatabase=new SoccerDatabase(this);
        matchDetail=(TextView) findViewById(R.id.matchView);
        highlightButton=(Button)findViewById(R.id.highlightButton);
        saveButton=(Button)findViewById(R.id.saveButton);

        Bundle bundle=getIntent().getExtras();
        String getTitle=bundle.getString("title");
        String getDate=bundle.getString("date");
        String getUrl=bundle.getString("url");
        matchDetail.setText("Match : "+getTitle+"\nTeam 1 : "+getTitle.split("-")[0]+"\nTeam 2 : "+getTitle.split("-")[1]+"\nDate : "+getDate);

        highlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse(getUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 soccerObject= new SoccerObject(getTitle, getDate, getUrl);
                long id = soccerDatabase.insertWord(getTitle, getDate, getUrl);
                soccerObject.setId(id);
                Toast.makeText(SoccerMatchDetail.this, "Match Added In Favourite List",LENGTH_LONG).show();
            }
        });
    }
}