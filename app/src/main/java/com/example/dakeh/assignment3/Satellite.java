package com.example.dakeh.assignment3;

import java.io.Serializable;

/**
 * Created by dakeh on 5/22/2016.
 */

public class Satellite {

    private String image_url;
    private String date;
    private String imgpath;

    String getDate() {return date;}

    void setDate(String date) {this.date = date;}

    String getBitmap() {return imgpath;}

    void setBitmap(String imgpath) {this.imgpath = imgpath;}

    String getImage_url() {
        return image_url;
    }

    void setImage_url(String image_url) {
        this.image_url = image_url;
    }



}


