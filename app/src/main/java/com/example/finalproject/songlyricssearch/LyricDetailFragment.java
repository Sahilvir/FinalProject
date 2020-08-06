package com.example.finalproject.songlyricssearch;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;

public class LyricDetailFragment extends Fragment {

    private AppCompatActivity parentActivity;
    private Bundle data;
    private View result;
    private TextView idText;
    private TextView artistText;
    private TextView songText;


    private Button hide;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = getArguments();

        View result = inflater.inflate(R.layout.lyric_fragment_layout,container,false);

        idText = result.findViewById(R.id.lyricFragmentId);
        artistText = result.findViewById(R.id.lyricFragmentArtistName);
        songText = result.findViewById(R.id.lyricFragmentSongName);
        hide = result.findViewById(R.id.lyricFragmentHide);

        idText.setText("ID = " + Long.toString(data.getLong(LyricMainActivity.SEARCH_ID)));
        artistText.setText(data.getString(LyricMainActivity.ARTIST_NAME));
        songText.setText(data.getString(LyricMainActivity.SONG_NAME));
        hide.setOnClickListener( e-> parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit());
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }

}
