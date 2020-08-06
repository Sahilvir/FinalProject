package com.example.finalproject.songlyricssearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.finalproject.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LyricResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Intent fromMain;
    private static final String LYRIC_API = "https://api.lyrics.ovh/v1/%s/%s";
    private TextView title;
    private EditText results;
    private Button save;
    private ProgressBar bar;
    private SongQuery songQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric_results);

        createToolbars();

        fromMain = getIntent();
        title = findViewById(R.id.lyricResultsTitle);
        results = findViewById(R.id.lyricLyrics);
        bar = findViewById(R.id.lyricProgressBar);
        save = findViewById(R.id.lyricSaveButton);
        String songName = fromMain.getStringExtra(LyricMainActivity.SONG_NAME);
        String artistName = fromMain.getStringExtra(LyricMainActivity.ARTIST_NAME);
        title.setText(songName + " - " + artistName);
        songQuery = new SongQuery();
        songQuery.execute(String.format(LYRIC_API,artistName,songName).replaceAll(" ","%20"));
        save.setOnClickListener(e->saveSearch(artistName,songName));
    }


    private void saveSearch(String artistName, String songName) {
        Intent goToSaved = new Intent(LyricResultsActivity.this, LyricSavedActivity.class);
        goToSaved.putExtra(LyricMainActivity.ARTIST_NAME,artistName);
        goToSaved.putExtra(LyricMainActivity.SONG_NAME,songName);
        startActivity(goToSaved);
    }



    protected class SongQuery extends AsyncTask<String, String, String> {

        private String lyrics;

        @Override
        protected String doInBackground(String... strings) {
            publishProgress(""+25);
            try {
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream response = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            publishProgress(""+50);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            publishProgress(""+75);
            String result = sb.toString();
            JSONObject lyricResults = new JSONObject(result);
            setLyrics(lyricResults.getString("lyrics"));
            Log.d("MyLog",getLyrics());

            } catch (Exception e) {
                Log.d("MyLog",e.getMessage());
            }

            publishProgress(""+100);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... strings) {
                bar.setProgress(Integer.parseInt(strings[0]));
                if (bar.getProgress() == 100)
                    bar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            results.setText((getLyrics() != null) ? getLyrics() : getString(R.string.not_found));
            Toast.makeText(LyricResultsActivity.this, (getLyrics()!=null) ? getString(R.string.toast_found): getString(R.string.not_found), Toast.LENGTH_LONG).show();
        }


        private void setLyrics(String lyrics) {
            this.lyrics = lyrics;
        }
        private String getLyrics() {
            return this.lyrics;
        }
    }


    public void createToolbars() {
        Toolbar toolbar = findViewById(R.id.lyricToolbar);
        NavigationView navigationView = findViewById(R.id.lyricNavigation);

        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.lyricDrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.lyric_open_drawer, R.string.lyric_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lyric_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.lyricToSoccer:
                //TODO:go to soccer activity
                break;
            case R.id.lyricToGeo:
                //TODO:go to geo activity
                break;
            case R.id.lyricToDeezer:
                //TODO: go to deezer activity
                break;
            case R.id.lyricAbout:
                Toast.makeText(this, R.string.lyrics_about,Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.InstructionForThisApp:
                new AlertDialog.Builder(this).setMessage(R.string.toolbar_instruction).setCancelable(true).show();
                break;
            case R.id.AboutApi:
                Intent goToWeb = new Intent(LyricResultsActivity.this,LyricWebSearchActivity.class);
                goToWeb.putExtra(LyricMainActivity.URL_KEY, LyricMainActivity.API_URL);
                startActivity(goToWeb);

                break;
            case R.id.DonateToProject:

                EditText donation = new EditText(this);
                donation.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(this).setMessage(R.string.donate_message)
                        .setPositiveButton(R.string.send_donation, (c, a) -> (Toast.makeText(this, R.string.contribution_received,Toast.LENGTH_LONG)).show())
                        .setNegativeButton(R.string.cancel,(c, a)->{}).setView(donation).show();
                break;
        }
        return true;
    }


}
