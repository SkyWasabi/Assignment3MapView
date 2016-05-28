package com.example.dakeh.assignment3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    private View view;
    private MapView mMapView;
    private Button historycalimage;
    private GoogleMap mMap;
    private float lon = 0;
    private float lat = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_map, container, false);
            MapsInitializer.initialize(this.getActivity());
            mMapView = (MapView) view.findViewById(R.id.googlemap);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
            historycalimage = (Button) view.findViewById(R.id.history);
            historycalimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Button clicked", Toast.LENGTH_SHORT).show();
                    SatelliteFragment satellitefragment = new SatelliteFragment();
                    satellitefragment.setLat(lat);
                    satellitefragment.setLon(lon);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.mapfrag, satellitefragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();
                    satellitefragment.performNASARequestSequence();
                }
            });
        }
        catch (InflateException e){
            e.printStackTrace();
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState); mMapView.onSaveInstanceState(outState);
    }
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Toast.makeText(getActivity(), "Marker clicked", Toast.LENGTH_SHORT).show();


        return false;
    }

    @Override
    public void onMapClick(LatLng latlng) {
        MarkerOptions markeroptions = new MarkerOptions();

        markeroptions.position(latlng);

        float latitude = (float)latlng.latitude;
        lat = latitude;

        float longitude = (float)latlng.longitude;
        lon = longitude;

        markeroptions.title(latlng.latitude + ":" + latlng.latitude);

        mMap.clear();

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));

        mMap.addMarker(markeroptions);


    }
}



