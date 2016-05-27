package com.example.dakeh.assignment3;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SatelliteFragment satelliteFragment = new SatelliteFragment();
        fragmentTransaction.add(R.id.satellite_container, satelliteFragment);
        fragmentTransaction.commit();


        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("Connected", "true");
            satelliteFragment.performNASARequestSequence();
        }

        else {
            Log.d("Connected", "false");
        }
    }




}

