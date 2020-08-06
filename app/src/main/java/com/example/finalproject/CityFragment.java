package com.example.finalproject;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CityFragment extends Fragment {

    AlertDialog.Builder alert;
    SQLiteDatabase db;
    CityDatabaseHelper helper;

    AppCompatActivity activity;
    private OnFragmentInteractionListener listener;

    public CityFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View save = inflater.inflate(R.layout.activity_city_fragment,container,false);
        TextView city = save.findViewById(R.id.frag_city);
        TextView country = save.findViewById(R.id.frag_country);
        TextView currency = save.findViewById(R.id.frag_currency);
        TextView region = save.findViewById(R.id.frag_region);
        TextView latitude = save.findViewById(R.id.frag_lat);
        TextView longitude = save.findViewById(R.id.frag_long);

        Bundle data = getArguments();
        String cityS = data.getString(SavedCities.CITY);
        String countryS = data.getString(SavedCities.COUNTRY);
        String currencyS = data.getString(SavedCities.CURRENCY);
        String regionS = data.getString(SavedCities.REGION);
        String latS = data.getString(SavedCities.LATITUDE);
        String longS = data.getString(SavedCities.LONGITUDE);

        city.setText(city.getText()+cityS);
        currency.setText(currency.getText()+currencyS);
        country.setText(country.getText()+countryS);
        region.setText(region.getText()+regionS);
        longitude.setText(longitude.getText()+longS);
        latitude.setText(latitude.getText()+latS);


//        Button delete = save.findViewById(R.id.delete_button);
//        delete.setOnClickListener(v -> {
//            alert = new AlertDialog.Builder(activity);
//            String data_id = Long.toString(id);
//
//            alert.setTitle("Do you want to delete this image?");
//            alert.setMessage("The selected row is: "+position+"\nThe database id is: "+id);
//            alert.setPositiveButton("Yes", (DialogInterface dialog, int which) ->{
//                db.delete(helper.TABLE,helper.ID+" =?",new String[]{data_id});
////                new SavedCities().removePosition(position);
//            });
//            alert.setNegativeButton("No",(DialogInterface dialog,int which) ->{
//            });
//            alert.show();
//        });

        Button maps= save.findViewById(R.id.maps2);
        maps.setOnClickListener(v -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/@" +latS+","+longS+",15z"));
            startActivity(intent);
        });
        Button hide = save.findViewById(R.id.hide);
        hide.setOnClickListener(v -> activity.getSupportFragmentManager().beginTransaction().remove(this).commit());
        return save;


    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener =null;
    }
}
