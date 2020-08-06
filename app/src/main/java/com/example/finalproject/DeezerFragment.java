package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeezerFragment extends Fragment {
    private Bundle dataFromActivity;
    private AppCompatActivity parentActivity;
    private String artistsName;
    private String songTitle;
    private String albumName;
    private String coverURL;
    private Bitmap cover;
    private int durSeconds;
    private String duration;
    private byte[] byteArray;
    private boolean isFav;
    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        dataFromActivity = getArguments(); //get the data from DeezerSearchMain.java

        //inflate layout for this fragment
        View result = inflater.inflate(R.layout.deezer_fragment, container, false);

        //retrieve byteArray and construct bitmap for album cover
        byteArray = dataFromActivity.getByteArray(DeezerSearchMain.FRAG_COVER);
        cover = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        //show cover
        ImageView coverImage = (ImageView) result.findViewById(R.id.deezerFragImage);
        coverImage.setImageBitmap(cover);

        //get title, album name and duration
        songTitle = dataFromActivity.getString(DeezerSearchMain.FRAG_TITLE);
        albumName = dataFromActivity.getString(DeezerSearchMain.FRAG_ALBUM);
        durSeconds = dataFromActivity.getInt(DeezerSearchMain.FRAG_DUR);
        duration = secondsToMinutes(durSeconds);

        //create Strings to be displayed
        String songTitle2 = getResources().getString(R.string.deezer_frag_titleText) + songTitle;
        String albumName2 = getResources().getString(R.string.deezer_frag_AlbumText) + albumName;
        String duration2 = getResources().getString(R.string.deezer_frag_DurText) + duration;

        //setText
        TextView title = (TextView) result.findViewById(R.id.deezerFragTitle);
        title.setText(songTitle2);
        TextView album = (TextView) result.findViewById(R.id.deezerFragAlbum);
        album.setText(albumName2);
        TextView dur = (TextView) result.findViewById(R.id.deezerFragDuration);
        dur.setText(duration2);

        //get artistsName and coverURL for saving to fav and adding to DB
        artistsName = dataFromActivity.getString(DeezerSearchMain.FRAG_ARTIST);
        coverURL = dataFromActivity.getString(DeezerSearchMain.FRAG_COVERURL);

        //check if fragment is being loaded from favourites list or not
        isFav = dataFromActivity.getBoolean(DeezerSearchMain.FRAG_FAV);

        //set click listener for Add to Favourites Button
        Button favButton = (Button) result.findViewById(R.id.deezerFragFav);
        favButton.setOnClickListener( click -> {
            //get database connection
            DeezerOpener deezerOpener = new DeezerOpener(getContext());
            db = deezerOpener.getWritableDatabase();

            //if isFav is true then we are accessing this fragment from the favourites list
            if(isFav){
                Snackbar.make(favButton, getResources().getString(R.string.deezer_frag_snackBar1), Snackbar.LENGTH_SHORT).show();
            }else{
                //adding favourites data to the database
                ContentValues favValues = new ContentValues();
                favValues.put(DeezerOpener.COL_ARTIST, artistsName);
                favValues.put(DeezerOpener.COL_TITLE, songTitle);
                favValues.put(DeezerOpener.COL_ALBUM, albumName);
                favValues.put(DeezerOpener.COL_DUR, durSeconds);
                favValues.put(DeezerOpener.COL_COVER, coverURL);
                long newId = db.insert(DeezerOpener.TABLE_NAME, null, favValues);

                Snackbar.make(favButton, songTitle + " " + getResources().getString(R.string.deezer_frag_snackBar2) + " " + artistsName + " " + getResources().getString(R.string.deezer_frag_snackBar3), Snackbar.LENGTH_SHORT).show();
            }


        });

        //get hide button and add click listener
        Button hideButton = (Button) result.findViewById(R.id.deezerFragHide);
        hideButton.setOnClickListener( click -> {
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result;
    }

    /** Converts seconds to String showing time in minutes Ex: 73 seconds -> 1:13
     *
     * @param seconds - the number of seconds being converted
     * @return - time in minutes as a String
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

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        parentActivity = (AppCompatActivity) context;
    }
}
