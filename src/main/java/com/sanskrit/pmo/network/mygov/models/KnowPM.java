package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

public class KnowPM {
    private static final String CONTENT = "content";
    private static final String IMAGE = "featured_image";
    private static final String LINK = "link";
    private static final String TITLE = "title";
    @SerializedName("content")
    public String mCOntent;
    @SerializedName("featured_image")
    public String mImage;
    @SerializedName("link")
    public String mLink;
    @SerializedName("title")
    public String mTitle;
}
