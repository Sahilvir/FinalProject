package com.example.finalproject.songlyricssearch;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;


public class LyricWebSearchActivity extends AppCompatActivity {

    private WebView web;
    private static final String URL = "https://www.google.com/search?q=%s+%s";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric_web_search);
        web = findViewById(R.id.lyricGoogleSearch);

        loadWebsite();
    }

    private void loadWebsite() {
        Intent fromMain = getIntent();
        String api = fromMain.getStringExtra(LyricMainActivity.URL_KEY);
        web.setWebViewClient(new WebViewClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl((api!=null)?api:String.format(URL,fromMain.getStringExtra(LyricMainActivity.ARTIST_NAME),fromMain.getStringExtra(LyricMainActivity.SONG_NAME)));
    }
}
