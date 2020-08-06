package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class Soccer_Favourite extends AppCompatActivity {

    private ListView listView;
    private List<String> savedMatchList = new ArrayList<>();
    private List<SoccerObject> savedMatchObjectList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    SoccerDatabase soccerDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer__favourite);

        listView = (ListView) findViewById(R.id.savedMatches);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, savedMatchList);
        soccerDatabase = new SoccerDatabase(this);
        savedMatchObjectList = soccerDatabase.getDatabase();
        for (int i = 0; i < savedMatchObjectList.size(); i++) {
            savedMatchList.add(savedMatchObjectList.get(i).getMatchTitle());
        }
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle=new Bundle();
            Intent intent=new Intent(this, SoccerSavedMatchDetail.class);
            bundle.putString("title",savedMatchObjectList.get(position).getMatchTitle());
            bundle.putString("date",savedMatchObjectList.get(position).getDate());
            bundle.putString("url",savedMatchObjectList.get(position).getUrl());
            bundle.putLong("id", savedMatchObjectList.get(position).getId());
            bundle.putInt("position",position);
            intent.putExtras(bundle);
            startActivityForResult(intent,8);
        }) ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8) {
            if (resultCode == RESULT_OK) {
                long id = data.getLongExtra("id", 0);
                int position = data.getIntExtra("position", 0);
                soccerDatabase.deleteWord(id);
                savedMatchList.remove(position);
                arrayAdapter.notifyDataSetChanged();

            }
        }
    }
}