package com.example.dakeh.assignment3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SatelliteFragment extends Fragment {

    TextView textView;
    ImageView mapview;

    private float lat;
    private float lon;
    private static String api_key = "11KwxbBt94WW26CWimobLPhaE4AgXxxhwxywTTXU";
    private URL api_url;
    private String savepath;
    private int counter = 0;
    private static int imgnum = 5;
    private ProgressDialog progress;
    private String [] datearray = new String[imgnum];
    private Bitmap [] img = new Bitmap[imgnum];
    private HashMap<String, Bitmap> data = new HashMap<String, Bitmap> ();
    private Timer t;
    private TimerTask timer;
    private int position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_satellite, container, false);
        mapview = (ImageView) view.findViewById(R.id.mapview);
        textView = (TextView) view.findViewById(R.id.labeltext);
//        t = new Timer();
//        t.scheduleAtFixedRate(timer, 0, 5000);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void performNASARequest(String date, float lon, float lat) {


        String url = "https://api.nasa.gov/planetary/earth/imagery?lon=" + String.valueOf(lon) + "&lat=" + String.valueOf(lat)
                + "&date=" + date + "&cloud_score=True&api_key=" + api_key;

        //Log.d("Check URL", url);

        //new DownloadTask().execute("https://api.nasa.gov/planetary/earth/imagery?lon=150.8931239&lat=-34.424984&date=2015-05-01&cloud_score=True&api_key=11KwxbBt94WW26CWimobLPhaE4AgXxxhwxywTTXU");

        new DownloadTask().execute(url);
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {
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
                String satellitedate = reader.getString("date");

                try {
                    Date temp = new SimpleDateFormat("yyyy-MM-dd").parse(satellitedate);
                    satellitedate = new SimpleDateFormat("yyyy-MM-dd").format(temp);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.d("URL", imageurl);

                new DownloadImageTask().execute(imageurl);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        private class DownloadImageTask extends  AsyncTask<String, Void, Bitmap> {

            protected Bitmap doInBackground(String... urls) {
                Bitmap mappic = null;
                try {
                    InputStream in = new URL(urls[0]).openStream();
                    mappic = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                img[counter] = mappic;
                data.put(datearray[counter], img[counter]);
                Log.d("date", String.valueOf(datearray[counter]));
                counter++;

                return mappic;

            }

            protected void onPostExecute(Bitmap result) {


                if (counter == imgnum) {
                    SetUI();
                }

            }

        }

    }

    public void performNASARequestSequence() {
        Date date = new Date();
        String sdate = "2015-05-03";

        SimpleDateFormat fomatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = fomatter.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        datearray[0] = getDate(date, 0);

//        //Log.d("Check date", adate);
//
        performNASARequest(datearray[0], lon, lat);

        datearray[1] = getDate(date, 16);
//        //Log.d("Check date", adate);
        performNASARequest(datearray[1], lon, lat);

        datearray[2] = getDate(date, 32);
        //Log.d("Check date", adate);
        performNASARequest(datearray[2], lon, lat);

        datearray[3] = getDate(date, 48);
        //Log.d("Check date", adate);
        performNASARequest(datearray[3], lon, lat);

        datearray[4] = getDate(date, 64);
        //Log.d("Check date", adate);
        performNASARequest(datearray[4], lon, lat);

    }

    public String getDate(Date date, int days) {
        String sdate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdate = sdf.format(calendar.getTime());

        return sdate;
    }

    public void SetUI() {

        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // As timer is not a Main/UI thread need to do all UI task on runOnUiThread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // increase your position so new image will show
                        position += 1;
                        // check whether position increased to length then set it to 0
                        // so it will show images in circuler
                        if (position >= imgnum)
                            position = 0;
                        // Set Image
                        mapview.setImageBitmap(data.get(datearray[position]));
                        textView.setText(datearray[position]);
                    }
                });
            }
        }, 0, 3000);
    }

    void setLon(float lon) {
        this.lon = lon;
    }

    float getLon() {
        return this.lon;
    }

    void setLat(float lat) {
        this.lat = lat;
    }

    float getLat() {
        return this.lat;
    }
}
