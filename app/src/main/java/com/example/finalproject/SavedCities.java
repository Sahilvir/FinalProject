package com.example.finalproject;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

public class SavedCities extends AppCompatActivity {

    public static final String COUNTRY = "country";
    public static final String REGION = "region";
    public static final String CURRENCY = "currency_code";
    public static final String LATITUDE = "longitude";
    public static final String LONGITUDE = "latitude";
    public static final String CITY = "city";
    CityAdapter adapter;
    Cursor c;
    CityDatabaseHelper helper;
    ListView list;
    AlertDialog.Builder alert;
    SQLiteDatabase db;
    ArrayList<Cities> citiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_cities);

        adapter = new CityAdapter(this,citiList);
        list = findViewById(R.id.saved_list);

        helper = new CityDatabaseHelper(this);
        db = helper.getWritableDatabase();
        c = db.query(helper.TABLE,new String[]{helper.ID,helper.CITY,helper.COUNTRY,helper.REGION,helper.CURRENCY,helper.LATITUDE,helper.LONGITUDE},null,null,null,null,null,null);
        LoadData();

        adapter.notifyDataSetChanged();
        list.setOnItemClickListener((AdapterView<?> innerList, View view, int position, long id) -> {
            Bundle data = new Bundle();
            data.putString(COUNTRY, citiList.get(position).getCountry());
            data.putString(CITY, citiList.get(position).getCity());
            data.putString(REGION, citiList.get(position).getRegion());
            data.putString(CURRENCY, citiList.get(position).getCurrency());
            data.putString(LATITUDE, citiList.get(position).getLat());
            data.putString(LONGITUDE, citiList.get(position).getLng());
            CityFragment details = new CityFragment();
            details.setArguments(data);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,details).commit();

        });

        list.setOnItemLongClickListener((AdapterView<?>parent,View v,int position, long id) -> {
            alert = new AlertDialog.Builder(this);
            String data_id = Long.toString(citiList.get(position).getId());
            alert.setTitle("Do you want to delete this city?");
            alert.setMessage("The selected row is: "+position+"\nThe database id is: "+id);
            alert.setPositiveButton("Yes", (DialogInterface dialog, int which) ->{
                db.delete(helper.TABLE,helper.ID+" =?",new String[]{data_id});
                citiList.remove(position);
                adapter.notifyDataSetChanged();
            });
            alert.setNegativeButton("No",(DialogInterface dialog,int which) ->{
            });
            alert.show();
            adapter.notifyDataSetChanged();
            return true;
        });

    }

    private void LoadData(){
        while(c.moveToNext()){
            long id = c.getLong(c.getColumnIndex(helper.ID));
            String city = c.getString(c.getColumnIndex(helper.CITY));
            String region = c.getString(c.getColumnIndex(helper.REGION));
            String country = c.getString(c.getColumnIndex(helper.COUNTRY));
            String currency = c.getString(c.getColumnIndex(helper.CURRENCY));
            String lat = c.getString(c.getColumnIndex(helper.LATITUDE));
            String lng = c.getString(c.getColumnIndex(helper.LONGITUDE));
            citiList.add(new Cities(id,country,region,city,currency,lat,lng));
            list.setAdapter(adapter);
        }
    }

    private class CityAdapter extends BaseAdapter {

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

