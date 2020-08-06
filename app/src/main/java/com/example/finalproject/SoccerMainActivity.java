package com.example.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class SoccerMainActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;

    private List<String> matchList=new ArrayList<>();
    private List<SoccerObject> matchObjectList=new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

    String title,date,embed,url=null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId== R.id.about) {
            View middle = getLayoutInflater().inflate(R.layout.activity_soccer_info, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.infoTextview);
            builder.setNegativeButton("Bingo", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.create().show();
            makeText(this, "App info", LENGTH_LONG).show();
        }

        if (itemId== R.id.fav) {
            Intent intent=new Intent(SoccerMainActivity.this,Soccer_Favourite.class);
            startActivity(intent);
            makeText(this, "Match list", LENGTH_LONG).show();
        }

        if (itemId== R.id.exit) {
            final Snackbar sb = Snackbar.make(listView, "Do You Want To Exit?", Snackbar.LENGTH_LONG);
            sb.setAction("Exit", new View.OnClickListener() {
                @Override
                public void onClick(View e) {
                    SoccerMainActivity.this.finish();
                }
            });
            sb.show();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.matchList);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,matchList);

        progressBar.setVisibility(View.VISIBLE);
        soccerAsync query = new soccerAsync();

        query.execute("https://www.scorebat.com/video-api/v1/");

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle=new Bundle();
            Intent intent=new Intent(this, SoccerMatchDetail.class);
            bundle.putString("title",matchObjectList.get(position).getMatchTitle());
            bundle.putString("date",matchObjectList.get(position).getDate());
            bundle.putString("url",matchObjectList.get(position).getUrl());
            intent.putExtras(bundle);
            startActivity(intent);
        }) ;
    }

    private  class soccerAsync extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPostExecute(String s) {

            listView.setAdapter(arrayAdapter);
            progressBar.setProgress(100);
            progressBar.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            String urlString = strings[0];
            String data=null;
            publishProgress(25);

            try {
                URL wordURL = new URL(urlString);
                urlConnection = (HttpURLConnection) wordURL.openConnection();
//                urlConnection.setReadTimeout(10000);
//                urlConnection.setConnectTimeout(15000);
//                urlConnection.setDoInput(true);
                urlConnection.connect();

//                OkHttpClient client = new OkHttpClient();

                BufferedReader reader = null;
                publishProgress(25);
//                Log.d("check0",data);
//                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                data=reader.readLine();
//                Log.d("check1",data);

                String json = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(), "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    urlConnection.getInputStream().close();
                    json = sb.toString();
                    Log.d("check4",json);
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                try {
                    publishProgress(50);
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        title=jObject.getString("title");
                        date=jObject.getString("date");
                        JSONArray videoObjectArray=jObject.getJSONArray("videos");
                        JSONObject videoObject= videoObjectArray.getJSONObject(0);
                        embed=videoObject.getString("embed");
                        url=Jsoup.parse(embed).select("iframe").attr("src");
                        matchList.add(title);
                        matchObjectList.add(new SoccerObject(title,date,url));
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            publishProgress(75);
            return "Data Getting";
        }


    }


}