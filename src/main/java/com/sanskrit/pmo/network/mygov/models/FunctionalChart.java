package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

public class FunctionalChart {
    private static final String CONTENT = "content";
    private static final String IMAGE = "featured_image";
    private static final String TITLE = "title";
    @SerializedName("content")
    public String mContent;
    @SerializedName("featured_image")
    public String mImage;
    @SerializedName("title")
    public String mTitle;
}
