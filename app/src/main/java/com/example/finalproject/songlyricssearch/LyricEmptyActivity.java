package com.example.finalproject.songlyricssearch;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.finalproject.R;
import com.google.android.material.navigation.NavigationView;


public class LyricEmptyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric_empty);
        createToolbars();
        Bundle data = getIntent().getExtras();

        LyricDetailFragment dFrag = new LyricDetailFragment();
        dFrag.setArguments(data);
        getSupportFragmentManager().beginTransaction().replace(R.id.lyricFragment,dFrag).commit();
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
                Intent goToWeb = new Intent(LyricEmptyActivity.this,LyricWebSearchActivity.class);
                goToWeb.putExtra(LyricMainActivity.URL_KEY,LyricMainActivity.API_URL);
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