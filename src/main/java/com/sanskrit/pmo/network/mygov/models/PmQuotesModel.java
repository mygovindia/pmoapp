package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class PmQuotesModel {
    private static final String CONTENT = "content";
    private static final String DATE = "date";
    private static final String ID = "ID";
    private static final String IMAGE = "image";
    private static final String LINK = "link";
    private static final String TITLE = "title";
    @SerializedName("content")
    public String mContent;
    @SerializedName("date")
    public String mDate;
    @SerializedName("ID")
    public String mID;
    @SerializedName("image")
    public String mImage;
    @SerializedName("link")
    public String mLink;
    @SerializedName("title")
    public String mTitle;
}
