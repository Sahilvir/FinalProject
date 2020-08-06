package com.example.finalproject.songlyricssearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.finalproject.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;


public class LyricMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private SharedPreferences sharedPreference;
    public static final String ARTIST_NAME = "ARTIST";
    public static final String SONG_NAME = "SONG";
    public static final String SEARCH_ID = "ID";
    public static final String URL_KEY = "URL";
    public static final String API_URL = "https://lyricsovh.docs.apiary.io/";
    private EditText artistName;
    private EditText songName;
    private Button instruction;
    private Switch searchSwitch;
    private Button search;
    private Button favourite;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric_main);
        sharedPreference = getPreferences(Context.MODE_PRIVATE);
        instruction = findViewById(R.id.lyricInstructionsButton);
        searchSwitch = findViewById(R.id.lyricSearchSwitch);
        artistName = findViewById(R.id.lyricArtistName);
        songName = findViewById(R.id.lyricSongName);
        search = findViewById(R.id.lyricSearchButton);
        favourite = findViewById(R.id.lyricFavouritesButton);

        createToolbars();
        loadSharedPreferences();


        instruction.setOnClickListener( e-> instructions());


        searchSwitch.setOnCheckedChangeListener( (cb,isChecked) -> {
            setSwitchState(isChecked);
            Snackbar.make(cb,getString((isChecked) ? R.string.snackbar_on_message : R.string.snackbar_off_message), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.snackbar_undo), click ->setSwitchState(!isChecked)).show();
        });


        search.setOnClickListener( e-> {
            if(searchSwitch.isChecked())
                google();
            else
                search();});


        favourite.setOnClickListener( e -> favourites());
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
                Intent goToWeb = new Intent(LyricMainActivity.this,LyricWebSearchActivity.class);
                goToWeb.putExtra(URL_KEY,API_URL);
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


    private void setSwitchState(boolean isChecked) {
        searchSwitch.setChecked(isChecked);
        searchSwitch.setText(getString((isChecked) ? R.string.search_switch_google : R.string.search_switch_default));
    }


    private void loadSharedPreferences() {

        String artist = sharedPreference.getString(ARTIST_NAME,null);
        String song = sharedPreference.getString(SONG_NAME,null);
        artistName.setText((artist != null) ? artist : "");
        songName.setText((song != null) ? song : "");
    }


    private void instructions() {
        new AlertDialog.Builder(this).setMessage(
                getString(R.string.lyric_instructions) + "\n\n"
                        + getString(R.string.search_button_appinstructions) +"\n\n"
                        + getString(R.string.google_search_button_instruction) + "\n\n"
                        + getString(R.string.save_button_instruction) + "\n\n"
                        + getString(R.string.favourites_button_instruction))
                .setCancelable(true).show();
    }


    private void search() {
        Intent goToResults = new Intent(LyricMainActivity.this, LyricResultsActivity.class);
        goToResults.putExtra(ARTIST_NAME,artistName.getText().toString());
        goToResults.putExtra(SONG_NAME,songName.getText().toString());
        startActivity(goToResults);
    }

    private void google() {
        Intent goToGoogle = new Intent(LyricMainActivity.this, LyricWebSearchActivity.class);
        goToGoogle.putExtra(ARTIST_NAME,artistName.getText().toString());
        goToGoogle.putExtra(SONG_NAME,songName.getText().toString());
        startActivity(goToGoogle);
    }


    private void favourites() {
        Intent goToSaved = new Intent(LyricMainActivity.this, LyricSavedActivity.class);
        startActivity(goToSaved);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //onPause simply saves shared preferences from artist and song name boxes
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(ARTIST_NAME, artistName.getText().toString());
        editor.putString(SONG_NAME, songName.getText().toString());
        editor.commit();
    }
}
