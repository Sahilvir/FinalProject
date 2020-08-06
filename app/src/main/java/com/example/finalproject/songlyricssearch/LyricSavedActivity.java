package com.example.finalproject.songlyricssearch;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;

public class LyricSavedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private Intent getSaved;
    private ListView savedList;
    private ArrayList<LyricSearch> list;
    private MyListAdapter myAdapter;
    private LyricOpener opener;
    private SQLiteDatabase db;
    private LyricDetailFragment dFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric_saved);
        savedList = findViewById(R.id.lyricSearchList);
        list = new ArrayList<>();
        myAdapter = new MyListAdapter(this,list);
        savedList.setAdapter(myAdapter);
        getSaved = getIntent();

        createToolbars();


        loadSearchesFromDatabase();
        if(getSaved.getExtras() != null)
            addSearch(getSaved.getStringExtra(LyricMainActivity.ARTIST_NAME),getSaved.getStringExtra(LyricMainActivity.SONG_NAME));

        savedList.setOnItemClickListener( (parent,view,pos,id) -> generateFragment(list.get(pos)));
        savedList.setOnItemLongClickListener( (parent,  view,  pos,  id) ->  {
            generateMainAlert(list.get(pos));
            return false;
        });
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
        //Options for drawer menu
        switch(item.getItemId()) {
            case R.id.InstructionForThisApp:
                new AlertDialog.Builder(this).setMessage(R.string.toolbar_instruction).setCancelable(true).show();
                break;
            case R.id.AboutApi:
                Intent goToWeb = new Intent(LyricSavedActivity.this,LyricWebSearchActivity.class);
                goToWeb.putExtra(LyricMainActivity.URL_KEY, LyricMainActivity.API_URL);
                startActivity(goToWeb);

                break;
            case R.id.DonateToProject:
                //Alert dialog with money edit text
                EditText donation = new EditText(this);
                donation.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(this).setMessage(R.string.donate_message)
                        .setPositiveButton(R.string.send_donation, (c, a) -> (Toast.makeText(this, R.string.contribution_received,Toast.LENGTH_LONG)).show())
                        .setNegativeButton(R.string.cancel,(c, a)->{}).setView(donation).show();
                break;
        }
        return true;
    }

    private void generateFragment(LyricSearch search) {
        Bundle data = new Bundle();
        data.putLong(LyricMainActivity.SEARCH_ID,search.getId());
        data.putString(LyricMainActivity.ARTIST_NAME, search.getArtistName());
        data.putString(LyricMainActivity.SONG_NAME, search.getSongName());

        Boolean isTablet = findViewById(R.id.lyricFragment) != null;
        if(isTablet) {
            dFrag = new LyricDetailFragment();
            dFrag.setArguments(data);
            getSupportFragmentManager().beginTransaction().replace(R.id.lyricFragment, dFrag).commit();
        } else {
            Intent goToEmpty = new Intent(LyricSavedActivity.this, LyricEmptyActivity.class);
            goToEmpty.putExtras(data);
            startActivity(goToEmpty);
        }
    }



    public void addSearch(String artist, String song) {

        ContentValues newRowValues = new ContentValues();
        newRowValues.put(LyricOpener.COL_ARTIST, artist);
        newRowValues.put(LyricOpener.COL_SONG,song);
        boolean empty = (artist == null || artist == "" || song == null || song == "");
        if(!empty) {
            long newId = db.insert(LyricOpener.TABLE_NAME, null, newRowValues);
            list.add(new LyricSearch(newId, artist, song));
            myAdapter.notifyDataSetChanged();
        }
    }

    public void deleteSearch(LyricSearch search) {
        db.delete(LyricOpener.TABLE_NAME, LyricOpener.COL_ID + "= ?",new String[]{Long.toString(search.getId())});
        list.remove(search);
        myAdapter.notifyDataSetChanged();
        if(dFrag != null)
            getSupportFragmentManager().beginTransaction().remove(dFrag).commit();
    }

    public void loadSearchesFromDatabase() {
        opener = new LyricOpener(this);
        db = opener.getWritableDatabase();

        String[] columns = {LyricOpener.COL_ID, LyricOpener.COL_ARTIST, LyricOpener.COL_SONG};
        Cursor results = db.query(false, LyricOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int idIndex = results.getColumnIndex(LyricOpener.COL_ID);
        int artistIndex = results.getColumnIndex(LyricOpener.COL_ARTIST);
        int songIndex = results.getColumnIndex(LyricOpener.COL_SONG);

        while(results.moveToNext()) {
            long id = results.getLong(idIndex);
            String artist = results.getString(artistIndex);
            String song = results.getString(songIndex);
            list.add(new LyricSearch(id,artist,song));
        }
    }

    private void generateMainAlert(LyricSearch saved) {
        new AlertDialog.Builder(this).setTitle("What would you like to do?").setMessage(saved.getArtistName() + " - " + saved.getSongName())
                .setNegativeButton("DELETE", (click,arg) ->  generateDeleteAlert(saved))
                .setNeutralButton("SEARCH", (click,arg) -> {
                    Intent goToResults = new Intent(LyricSavedActivity.this, LyricResultsActivity.class);
                    goToResults.putExtra(LyricMainActivity.ARTIST_NAME,saved.getArtistName());
                    goToResults.putExtra(LyricMainActivity.SONG_NAME,saved.getSongName());
                    startActivity(goToResults);})
                .setPositiveButton("CANCEL", (click,arg) -> {}).create().show();
    }


    public void generateDeleteAlert(LyricSearch saved) {
        new AlertDialog.Builder(this).setTitle("Confirm").setMessage("Are you sure you wish to delete?")
                .setPositiveButton("Yes", (e,i) ->
                    deleteSearch(saved))
                .setNegativeButton("No",(e,i)->{}).show();
    }

    public class MyListAdapter extends BaseAdapter {


        private Context context;
        private ArrayList<LyricSearch> list;
        protected MyListAdapter(Context context, ArrayList<LyricSearch> list) {
            setContext(context);
            setList(list);
        }

        @Override
        public int getCount() {
            return getList().size();
        }

        @Override
        public Object getItem(int position) {
            return getList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return getList().get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.lyric_search_layout,parent,false);

            TextView songName = convertView.findViewById(R.id.lyricSongText);
            TextView artistName = convertView.findViewById(R.id.lyricArtistText);

            songName.setText(getList().get(position).getSongName());
            artistName.setText(getList().get(position).getArtistName());

            return convertView;
        }


        private void setContext(Context context) {
            this.context = context;
        }
        private Context getContext() {
            return this.context;
        }
        private void setList(ArrayList<LyricSearch> list) {
            this.list = list;
        }
        private ArrayList<LyricSearch> getList() {
            return this.list;
        }
    }
}
