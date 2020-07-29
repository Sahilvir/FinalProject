package com.example.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class favCity extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_weather_forecast);
//        progressBar = findViewById(R.id.weatherbar);
        progressBar.setVisibility(View.VISIBLE);


        ForecastQuery forecastQuery = new ForecastQuery();
        forecastQuery.execute();

    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {

        float uv;
        String  min, max, current;
        Bitmap currentW;

        @Override
        protected String doInBackground(String... strings) {

            try {
                //create a URL object of what server to contact:
                URL url = new URL("https://api.geodatasource.com/cities");

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();



                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");


                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT) {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("temperature")) {
                            //If you get here, then you are pointing to a <Weather> start tag
                            current = xpp.getAttributeValue(null,    "value");
                            publishProgress(25);
                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                        }

                        if(xpp.getName().equals("weather"))
                        {
                            //If you get here, then you are pointing to a <Weather> start tag
                            String iconName = xpp.getAttributeValue(null,    "icon");
                            String urlString = "http://openweathermap.org/img/w/"+iconName+".png";
                            URL url1 = new URL(urlString);
                            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                            connection.connect();
                            int responseCode = connection.getResponseCode();
                            if (responseCode == 200) {
                                currentW = BitmapFactory.decodeStream(connection.getInputStream());

                                if (!(fileExistance(iconName+".png"))){/////////////////////////////////
                                    FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                                    currentW.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                    Log.e("Filename : "+iconName+".png","File downloaded");

                                }else{
                                    FileInputStream fis = null;
                                    try {
                                        fis = openFileInput(iconName+".png");
                                        publishProgress(100);
                                        Log.e("Filename : "+iconName+".png","File found in storage");
                                    }
                                    catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    currentW = BitmapFactory.decodeStream(fis);
                                }

                            }

                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }
            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
                return "finished";
            }
            try {

                //create a URL object of what server to contact:
                URL url = new URL("https://api.geodatasource.com/cities");

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();


                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON:
                JSONObject jObject = new JSONObject(result);

                //get the double associated with "value"
                uv = (float)jObject.getDouble("value");

                Log.i("MainActivity", "The uv is now: " + uv) ;

            }
            catch (Exception e)
            {

            }

            return "Done";
        }



        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onPostExecute(String s) {

            TextView textView = findViewById(R.id.current);
            textView.setText("Current Temperature:\t" + current);

            textView = findViewById(R.id.max);
            textView.setText("Max Temperature:\t" + max);

            textView = findViewById(R.id.min);
            textView.setText("Max Temperature:\t" + min);

            textView = findViewById(R.id.uv);
            textView.setText("UV Rating:\t" + uv);

            ImageView imageView = findViewById(R.id.weatherview);
            imageView.setImageBitmap(currentW);

            ProgressBar progressBar = findViewById(R.id.weatherbar);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            ProgressBar progressBar = findViewById(R.id.weatherbar);
            progressBar.setProgress(values[0]);
        }
    }
}