package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/** This is the main page for the Deezer Song Search activity in the application
 *
 */

public class DeezerSearchMain extends AppCompatActivity {

    public EditText artistName; //EditText box that users will put artist names into
    public SharedPreferences pref; //SharedPreferences to store artist name for next launch
    public String artist; //stores artist's name
    public String artistsName; //for storing artist's name obtained from JSON data
    public final static String myPreference = "myPref";
    public final static String Artist = "artistKey";
    public final static String FRAG_FAV = "isFav";
    public final static String FRAG_TITLE = "SongTitle";
    public final static String FRAG_ALBUM = "Album";
    public final static String FRAG_DUR = "Duration";
    public final static String FRAG_COVERURL = "CoverURL";
    public final static String FRAG_ARTIST = "ArtistName";
    public final static String FRAG_COVER = "Cover";
    public boolean isFav;
    private ArrayList<DeezerSong> deezerSongList = new ArrayList<>();
    public ArrayList<DeezerSong> deezerFavList = new ArrayList<>();
    private DeezerAdapter adapter = new DeezerAdapter(this, deezerSongList); //adapter for the search list
    private DeezerAdapter adapter2 = new DeezerAdapter(this, deezerFavList); //adapter for the fav list
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_search_main);

        Intent fromMain = getIntent();

        //setting the EditText to be what is stored in pref
        artistName = (EditText) findViewById(R.id.deezerSearchEditText);
        pref = getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        //only set text if there is something contained in the pref file
        if(pref.contains(Artist)){
            artistName.setText(pref.getString(Artist, ""));
        }

        //declaring the buttons
        Button helpButton = (Button) findViewById(R.id.deezerHelpButton);
        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button favButton = (Button) findViewById(R.id.deezerFavouritesButton);

        //getting the ListView
        ListView myList = (ListView) findViewById(R.id.deezerListView);

        //help button onClickListener
        helpButton.setOnClickListener( click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(DeezerSearchMain.this);
            builder.setTitle(getResources().getString(R.string.deezer_main_helpBtnAppIns));
            builder.setMessage(getResources().getString(R.string.deezer_main_helpBtnAppIns2));
            builder.setNegativeButton(getResources().getString(R.string.deezer_main_helpBtnClose), (click2, arg) -> {});
            builder.create().show();
        });

        //search button onClickListener
        searchButton.setOnClickListener( click -> {
            isFav = false; //lets us know we are not loading the favourites list
            myList.setAdapter(adapter); //sets the adapter
            artist = emptyTextBox(); //empties EditText and sets artist to the artist name searched
            deezerSongList.clear(); //clears the listView before showing new search results
            adapter.notifyDataSetChanged(); //updates listview
            //construct the URL to search the API
            String deezerURL = "https://api.deezer.com/search/artist/?q=" + artist + "&output=xml";
            DeezerQuery req = new DeezerQuery(); //make new DeezerQuery AsyncTask
            req.execute(deezerURL); //execute using the URL constructed above
        });

        //favourites button onClickListener
        favButton.setOnClickListener( click -> {
            isFav = true; //lets us know we are in the favourites list

            onPause();  //add the last search query back to the search EditText
            if(pref.contains(Artist)){
                artistName.setText(pref.getString(Artist, ""));
            }

            myList.setAdapter(adapter2); //sets the adapter
            deezerFavList.clear(); //clears list before adding all data in database to deezerFavList
            DeezerFavQuery deezerFavs = new DeezerFavQuery();
            deezerFavs.execute();
            adapter2.notifyDataSetChanged(); //update listview
        });

        //loads the fragment with data when an item in the list is clicked
        myList.setOnItemClickListener((list, item, position, id) -> {
            //creating a bundle to send data to the fragment
            Bundle dataToPass = new Bundle();

            //let the fragment know whether it is being opened from favourites list or not
            dataToPass.putBoolean(FRAG_FAV, isFav);

            //pass data depending on which list we are getting data from
            if(isFav){
                dataToPass.putString(FRAG_TITLE, deezerFavList.get(position).getTitle());
                dataToPass.putString(FRAG_ALBUM, deezerFavList.get(position).getAlbum());
                dataToPass.putInt(FRAG_DUR, deezerFavList.get(position).getDuration());
                dataToPass.putString(FRAG_COVERURL, deezerFavList.get(position).getCoverURL());
                dataToPass.putString(FRAG_ARTIST, artistsName);

                //converting Bitmap to byteArray so it can be passed to the fragment in the bundle
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                deezerFavList.get(position).getCover().compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                dataToPass.putByteArray(FRAG_COVER, byteArray);
            }else {
                dataToPass.putString(FRAG_TITLE, deezerSongList.get(position).getTitle());
                dataToPass.putString(FRAG_ALBUM, deezerSongList.get(position).getAlbum());
                dataToPass.putInt(FRAG_DUR, deezerSongList.get(position).getDuration());
                dataToPass.putString(FRAG_COVERURL, deezerSongList.get(position).getCoverURL());
                dataToPass.putString(FRAG_ARTIST, artistsName);

                //converting Bitmap to byteArray so it can be passed to the fragment in the bundle
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                deezerSongList.get(position).getCover().compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                dataToPass.putByteArray(FRAG_COVER, byteArray);
            }

            //creating the fragment and setting the data
            DeezerFragment dFragment = new DeezerFragment();
            dFragment.setArguments(dataToPass);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.deezerFrameLayout, dFragment, "FRAGMENT")
                    .commit();
        });

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(isFav) {
                    //alert dialog that asks whether or not the user wants to delete the song from their favourites list
                    AlertDialog.Builder builder = new AlertDialog.Builder(DeezerSearchMain.this);
                    builder.setTitle(getResources().getString(R.string.deezer_main_delBtnDialog));
                    builder.setMessage(getResources().getString(R.string.deezer_main_delBtnInfo) + " " + deezerFavList.get(position).getTitle() + " " + getResources().getString(R.string.deezer_main_delBtnInfo2) + " " + deezerFavList.get(position).getArtistName() + ".");
                    builder.setPositiveButton(getResources().getString(R.string.deezer_main_delYes), (click, arg) -> {
                        db.delete(DeezerOpener.TABLE_NAME, DeezerOpener.COL_ID + "= ?", new String[]{Long.toString(deezerFavList.get(position).getId())});
                        deezerFavList.remove(position);
                        adapter2.notifyDataSetChanged();
                    });
                    builder.setNegativeButton(getResources().getString(R.string.deezer_main_delNo), (click, arg) -> {
                    });
                    builder.create().show();
                    return true;
                }
                return false;
            }
        });

        //setting progressBar to invisible
        ProgressBar progBar = (ProgressBar) findViewById(R.id.deezerProgressBar);
        progBar.setVisibility(View.INVISIBLE);
    }

    /** Empties the EditText box and returns the String that was written inside it
     * @return artist name in search box as String
     */
    public String emptyTextBox(){
        artistName = (EditText) findViewById(R.id.deezerSearchEditText);
        String name = artistName.getText().toString();
        artistName.setText("");
        return name;
    }

    /** Converts seconds to minutes 73 -> 1:13
     *
     * @param seconds - total number of seconds
     * @return - String of duration in above format
     */
    public String secondsToMinutes(int seconds){
        int minutes = 0;
        while(seconds >= 60){
            seconds -= 60;
            minutes++;
        }
        String sec;
        if(seconds < 10){
            sec = "0" + seconds;
        }else{
            sec = seconds + "";
        }
        String time = minutes + ":" + sec;
        return time;
    }

    //loads data from the database and adds it to the favourites list
    private void loadDataFromDatabase(){
        //get database connection
        DeezerOpener deezerOpener = new DeezerOpener(this);
        db = deezerOpener.getWritableDatabase();

        //define columns and create cursor
        String [] columns = {DeezerOpener.COL_ID, DeezerOpener.COL_ARTIST, DeezerOpener.COL_TITLE, DeezerOpener.COL_ALBUM, DeezerOpener.COL_DUR, DeezerOpener.COL_COVER};
        Cursor results = db.query( DeezerOpener.TABLE_NAME, columns, null, null, null, null, null);

        //get column indices
        int idColIndex = results.getColumnIndex(DeezerOpener.COL_ID);
        int artistColIndex = results.getColumnIndex(DeezerOpener.COL_ARTIST);
        int titleColIndex = results.getColumnIndex(DeezerOpener.COL_TITLE);
        int albumColIndex = results.getColumnIndex(DeezerOpener.COL_ALBUM);
        int durColIndex = results.getColumnIndex(DeezerOpener.COL_DUR);
        int coverColIndex = results.getColumnIndex(DeezerOpener.COL_COVER);

        //loop through the database obtaining data
        while(results.moveToNext()){
            long id = results.getLong(idColIndex);
            String artistDB = results.getString(artistColIndex);
            String titleDB = results.getString(titleColIndex);
            String albumDB = results.getString(albumColIndex);
            int durationDB = results.getInt(durColIndex);
            String coverURL_DB = results.getString(coverColIndex);

            //create the cover Bitmap
            Bitmap coverDB = getBitmapFromURL(coverURL_DB);

            //add to favourites list
            deezerFavList.add(new DeezerSong(titleDB, albumDB, artistDB, coverURL_DB, coverDB, durationDB, id));
        }
    }

    /** Stores the text in the search box for next launch
     *  If the search box is empty, it stores the last artist name entered
     */
    @Override
    public void onPause(){
        super.onPause();

        if(artistName.getText().toString().equals("")){
            //puts the last entered artist's name into pref if nothing is currently written in the EditText
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(Artist, artist);
            edit.commit();
        }else {
            //puts whatever is currently written in the EditText into pref
            String artistEditText = artistName.getText().toString();
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(Artist, artistEditText);
            edit.commit();
        }
    }


    /**
     * This method will return a Bitmap object from a given URL
     * @param src is the source URL in String form
     * @return the Bitmap object obtained from the URL
     */
    public Bitmap getBitmapFromURL(String src){
        try{
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private class DeezerFavQuery extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                loadDataFromDatabase();
            }catch(Exception e){
                e.printStackTrace();
            }
            return "Done";
        }

        public void onPostExecute(String fromDoInBackground){

            //update the listView
            adapter2.notifyDataSetChanged();

            Log.i("HTTP", fromDoInBackground);
        }
    }

    /** Nested AsyncTask class for creating connections to the deezer song search API
     *
     */
    private class DeezerQuery extends AsyncTask<String, Integer, String> {
        public static final String ACTIVITY_NAME = "DEEZER_SEARCH_ACTIVITY";
        String trackURL;
        String title;
        String album;
        String coverURL;
        Bitmap cover;
        int duration;


        @Override
        protected String doInBackground(String... args) {
            try{
                //URL for deezer search
                URL url = new URL(args[0]); //xml

                //open connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); //xml

                //wait for data
                InputStream response = urlConnection.getInputStream(); // xml

                //reading the xml
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");

                int eventType = xpp.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT){
                    if(eventType == XmlPullParser.START_TAG){
                        if(xpp.getName().equalsIgnoreCase("tracklist")){
                            xpp.next(); //moves to the URL
                            trackURL = xpp.getText(); //will return the URL

                            //only gives the first result for now, could be changed to store these URLs in an ArrayList
                            //which could then be used to display all results
                            break; //exits loop
                        }
                    }
                    eventType = xpp.next(); //move to next XML event
                }

                //making connection to the tracklist JSON
                URL url2 = new URL(trackURL); //song list URL
                HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
                InputStream response2 = urlConnection2.getInputStream();

                //to read the JSON, response2 is read and then put into a String using a StringBuilder line by line
                BufferedReader reader = new BufferedReader(new InputStreamReader(response2));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //store the result into a JSONObject
                JSONObject trackData = new JSONObject(result);

                //query the data array (this array contains the whole list of songs)
                JSONArray allSongData = trackData.getJSONArray("data");

                //loop through the array to get data for each song
                for(int i = 0; i < allSongData.length(); i++){
                    JSONObject songData = allSongData.getJSONObject(i);
                    title = songData.getString("title");
                    publishProgress(20);
                    duration = songData.getInt("duration");
                    publishProgress(40);

                    //artist data is stored inside a JSONObject
                    JSONObject artistData = songData.getJSONObject("artist");
                    artistsName = artistData.getString("name");
                    publishProgress(50);

                    //album data is stored inside another JSONObject within the songData
                    JSONObject albumData = songData.getJSONObject("album");
                    album = albumData.getString("title"); //album title
                    publishProgress(60);
                    coverURL = albumData.getString("cover_small"); //get the small cover as this will be used in a ListView
                    cover = getBitmapFromURL(coverURL); //create bitmap for the cover
                    publishProgress(80);

                    //store data into deezerSong object and add it to the ArrayList
                    DeezerSong deezerSong = new DeezerSong(title, album, artistsName, coverURL, cover, duration);
                    deezerSongList.add(deezerSong);
                    publishProgress(100);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            return "Done";
        }

        public void onProgressUpdate(Integer ... value){
            ProgressBar progBar = findViewById(R.id.deezerProgressBar);
            progBar.setVisibility(View.VISIBLE);
            progBar.setProgress(value[0]);
        }

        public void onPostExecute(String fromDoInBackground){
            //hiding progressBar
            ProgressBar progBar = findViewById(R.id.deezerProgressBar);
            progBar.setVisibility(View.INVISIBLE);

            //update the listView
            adapter.notifyDataSetChanged();

            //toast message to notify user the search is complete
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.deezer_main_searchComplete), Toast.LENGTH_SHORT);
            toast.show();

            Log.i("HTTP", fromDoInBackground);
        }
    }

    /** Adapter class for populating the ListView
     *
     */

    public class DeezerAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<DeezerSong> list;

        //constructor
        public DeezerAdapter(Context context, ArrayList<DeezerSong> list){
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //inflate the view
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.deezer_listview_search, parent, false);
            DeezerSong currentSong = (DeezerSong) getItem(position);

            //sets the image and text for each ListView row
            ImageView coverImage = (ImageView) newView.findViewById(R.id.albumCoverImage);
            coverImage.setImageBitmap(currentSong.getCover());
            TextView songNameText = (TextView) newView.findViewById(R.id.songName);
            songNameText.setText(currentSong.getTitle());
            TextView artistNameText = (TextView) newView.findViewById(R.id.songArtist);
            artistNameText.setText(currentSong.getArtistName());

            return newView;
        }
    }
}