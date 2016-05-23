package com.example.dakeh.assignment3;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.StringTokenizer;

public class SatelliteFragment extends Fragment {

    TextView textView;
    ImageView mapview;

    private float lat;
    private float lon;
    private static String api_key = "11KwxbBt94WW26CWimobLPhaE4AgXxxhwxywTTXU";
    private URL api_url;
    private String savepath;
    private int index = 0;

    ArrayList<Satellite> satelliteArrayList = new ArrayList<Satellite>();;

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
        Satellite satellite = new Satellite();

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

                satellite.setImage_url(imageurl);
                satellite.setDate(satellitedate);

                //Log.d("date", satellitedate);
                new DownloadImageTask().execute(satellite.getImage_url());
                satelliteArrayList.add(satellite);
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

                savepath = saveToInternalStorage(mappic);
                Log.d("Path", savepath);
                satellite.setBitmap(savepath);
                satelliteArrayList.add(satellite);
                return mappic;
            }

            protected void onPostExecute(Bitmap result) {


            }
        }

        private String saveToInternalStorage(Bitmap bitmap) {

            ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
            File directory = cw.getDir("imageDir" + String.valueOf(index) , Context.MODE_PRIVATE);
            File mypath = new File(directory, "satpic.jpg");

            FileOutputStream fos;

            try {
                fos = new FileOutputStream(mypath);

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Log.d("Save", "Complete");
                fos.close();
                index++;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return directory.getAbsolutePath();
        }
    }



    public void performNASARequestSequence() {

        Date date = new Date();
        String sdate = "2015-05-01";

        SimpleDateFormat fomatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = fomatter.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        lon = 150.8931239f;
        lat = -34.424984f;

        String adate = getDate(date, 0);

        performNASARequest(adate, lon, lat);

        adate = getDate(date, 16);
        //Log.d("Check date", adate);
        performNASARequest(adate, lon, lat);

        adate = getDate(date, 32);
        //Log.d("Check date", adate);
        performNASARequest(adate, lon, lat);

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
        Log.d("Size", String.valueOf(satelliteArrayList.size()));
        String path = satelliteArrayList.get(satelliteArrayList.size()).getBitmap();

        loadImageFromStorage(path);
    }

    private void loadImageFromStorage(String path) {
        try {
            File f = new File(path, "satpic.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            mapview.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
