package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Post {

    private static final String CONTENT = "content";
    private static final String DATE = "date";
    private static final String EXCERPT = "excerpt";
    private static final String ID = "ID";
    private static final String FEATURE_IMAGE = "feature_image";
    private static final String TITLE = "title";
    @SerializedName("content")
    public String content;
    @SerializedName("date")
    public String date;
    @SerializedName("excerpt")
    public String excerpt;
    @SerializedName("ID")
    public int id;
    @SerializedName("feature_image")
    public String feature_image;
    @SerializedName("title")
    public String title;


}
