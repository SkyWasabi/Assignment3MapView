package com.example.dakeh.assignment3;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        com.example.dakeh.assignment3.MapFragment mapFragment = new com.example.dakeh.assignment3.MapFragment();
        fragmentTransaction.add(R.id.mapfrag, mapFragment);

//
//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);


//        SatelliteFragment satelliteFragment = new SatelliteFragment();
//        fragmentTransaction.add(R.id.satellite_container, satelliteFragment);
        fragmentTransaction.commit();


        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("Connected", "true");
//            satelliteFragment.performNASARequestSequence();
        }

        else {
            Log.d("Connected", "false");
        }
    }

//    @Override
//    public void onMapReady(GoogleMap map) {
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(0, 0))
//                .title("Marker"));
//    }



}

