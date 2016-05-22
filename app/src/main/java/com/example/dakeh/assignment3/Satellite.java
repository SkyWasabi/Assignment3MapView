package com.example.dakeh.assignment3;

import java.net.URL;

/**
 * Created by dakeh on 5/22/2016.
 */

public class Satellite {
    private float lat;
    private float lon;
    private String api_key = "11KwxbBt94WW26CWimobLPhaE4AgXxxhwxywTTXU";
    private URL api_url;

    float getLat() {
        return lat;
    }

    float getLon() {
        return lon;
    }

    void setLat(float lat) {
        this.lat = lat;
    }

    void setLon(float lon) {
        this.lon = lon;
    }

    String getApi_key() {
        return api_key;
    }

    void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    URL getApi_url() {
        return api_url;
    }

    void setApi_url(URL api_url) {
        this.api_url = api_url;
    }



}


