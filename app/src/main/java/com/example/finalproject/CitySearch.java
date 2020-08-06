package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CitySearch extends AppCompatActivity {

    static final String API = "https://api.geodatasource.com/cities?key=EDCL0N4J10D0QVPQ7HWFUEUXIVEJRH3Z&format=JSON";
    ListView list;
    CityAdapter adapter;
    ProgressBar loading;
    AlertDialog.Builder alert;

    ArrayList<Cities> citiList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);
        list = findViewById(R.id.list);
        String latitude = getIntent().getStringExtra("lat");
        String longitude = getIntent().getStringExtra("lng");
        adapter = new CityAdapter(this,citiList);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        CityQuery query = new CityQuery();
        query.execute(API+"&lat="+latitude+"&lng="+longitude);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Button help = findViewById(R.id.help);
        help.setOnClickListener(v -> {
            alert = new AlertDialog.Builder(this);
            alert.setTitle("Help");
            alert.setMessage("To view the details of a City, click on it.\nYou can view its details, save it to your list and view the location on Google Maps.");
            alert.setNeutralButton("Got It", (DialogInterface dialog, int which) -> {

            });
            alert.show();
        });

        list.setOnItemClickListener((p, v, position, id)-> {
            Intent nextActivity = new Intent(this,Details.class);
            nextActivity.putExtra("city",citiList.get(position).getCity());
            nextActivity.putExtra("country",citiList.get(position).getCountry());
            nextActivity.putExtra("region",citiList.get(position).getRegion());
            nextActivity.putExtra("currency",citiList.get(position).getCurrency());
            nextActivity.putExtra("latitude",citiList.get(position).getLat());
            nextActivity.putExtra("longitude",citiList.get(position).getLng());
            startActivity(nextActivity);
        });

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
    }

    private class CityQuery extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url1 = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                InputStream stream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader buffer = new BufferedReader(reader);
                StringBuilder stringBuilder = new StringBuilder();
                String line ;
                while((line = buffer.readLine())!=null){
                    stringBuilder.append(line+"\n");
                }
                publishProgress(50);
                String result = stringBuilder.toString();
                JSONArray initi = new JSONArray(result);
                for(int i = 0; i < initi.length(); i++)
                {
                    JSONObject obj = initi.getJSONObject(i);
                    citiList.add(new Cities(i,obj.getString("country"),obj.getString("region"),obj.getString("city"),obj.getString("currency_code"),obj.getString("latitude"),obj.getString("longitude")));

                }
                publishProgress(100);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "finished";
        }

        @Override
        protected void onPostExecute(String s) {
            adapter.notifyDataSetChanged();
            super.onPostExecute(s);
            loading.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            loading.setVisibility(View.VISIBLE);
            loading.setProgress(values[0]);
        }
    }

    private class CityAdapter extends BaseAdapter{

        public CityAdapter(Context context, ArrayList<Cities> list){}

        @Override
        public int getCount() {
            return citiList.size();
        }

        @Override
        public Cities getItem(int position) {
            return citiList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String city = getItem(position).getCity();
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.city,parent,false);
            TextView t = row.findViewById(R.id.city_name);
            t.setText(city);
            return row;
//            return null;
        }
    }
}