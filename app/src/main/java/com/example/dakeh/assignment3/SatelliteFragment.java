package com.example.dakeh.assignment3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;

public class SatelliteFragment extends Fragment {

    private float lat;
    private float lon;
    private String api_key = "11KwxbBt94WW26CWimobLPhaE4AgXxxhwxywTTXU";
    private URL api_url;

    public SatelliteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_satellite, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
