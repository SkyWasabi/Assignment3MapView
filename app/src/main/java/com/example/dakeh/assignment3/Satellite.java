package com.example.dakeh.assignment3;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by dakeh on 5/22/2016.
 */

public class Satellite {

    private String image_url;
    private String date;
    private Bitmap satpic;

    String getDate() {return date;}

    void setDate(String date) {this.date = date;}

    Bitmap getBitmap() {return satpic;}

    void setBitmap(Bitmap satpic) {this.satpic = satpic;}

    String getImage_url() {
        return image_url;
    }

    void setImage_url(String image_url) {
        this.image_url = image_url;
    }



}


