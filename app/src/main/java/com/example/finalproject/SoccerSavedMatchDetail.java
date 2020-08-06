package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SoccerSavedMatchDetail extends AppCompatActivity {
    private TextView matchDetail;
    private Button DeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_saved_match_detail);
        matchDetail=(TextView) findViewById(R.id.matchView);
        DeleteButton=(Button)findViewById(R.id.deleteButton);

        Bundle bundle=getIntent().getExtras();
        String getTitle=bundle.getString("title");
        String getDate=bundle.getString("date");
        String getUrl=bundle.getString("url");
        long id=bundle.getLong("id");
        int position=bundle.getInt("position");
        matchDetail.setText("Match : "+getTitle+"\nTeam 1 : "+getTitle.split("-")[0]+"\nTeam 2 : "+getTitle.split("-")[1]+"\nDate : "+getDate);


        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         Intent intent=new Intent();
         intent.putExtra("id",id);
         intent.putExtra("position",position);
         SoccerSavedMatchDetail.this.setResult(Activity.RESULT_OK,intent);
                SoccerSavedMatchDetail.this.finish();
            }
        });
    }

}
