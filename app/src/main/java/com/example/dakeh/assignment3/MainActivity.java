package com.example.dakeh.assignment3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.HandlerThread;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ImageView mapview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SatelliteFragment satelliteFragment = new SatelliteFragment();
        fragmentTransaction.add(R.id.satellite_container, satelliteFragment);
        fragmentTransaction.commit();


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        textView = (TextView) findViewById(R.id.labeltext);
        mapview = (ImageView) findViewById(R.id.mapview);

        if (networkInfo != null && networkInfo.isConnected()) {
            new performNASARequest().execute("https://api.nasa.gov/planetary/earth/imagery?lon=150.8931239&lat=-34.424984&date=2015-05-01&cloud_score=True&api_key=11KwxbBt94WW26CWimobLPhaE4AgXxxhwxywTTXU");
            Log.d("Connected", "true");
        }

        else {
            Log.d("Connected", "false");
        }
    }

    private class performNASARequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return performRequest(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.v("tag", result);
            //textView.setText(result);
            fetchImage(result);
        }

        private String performRequest(String myURL) throws  IOException {
            InputStream in = null;

            try {
                URL url = new URL(myURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                int response = connection.getResponseCode();

                Log.v("Response code", String.valueOf(response));

                in = connection.getInputStream();
                String stringResult = parseStream(in);

                return stringResult;
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }

        public  String parseStream(InputStream stream) throws  IOException, UnsupportedEncodingException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        }

        public void fetchImage(String result) {
            try {
                JSONObject reader = new JSONObject(result);

                String imageurl = reader.getString("url");
                //Log.d("image url", imageurl);
                new DownloadImageTask(mapview).execute(imageurl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadImageTask extends  AsyncTask<String, Void, Bitmap> {
        ImageView download;
        public DownloadImageTask(ImageView download) {
            this.download = download;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap mappic = null;
            try {
                InputStream in = new URL(urls[0]).openStream();
                mappic = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mappic;
        }

        protected void onPostExecute(Bitmap result) {
            download.setImageBitmap(result);
        }
    }

}

