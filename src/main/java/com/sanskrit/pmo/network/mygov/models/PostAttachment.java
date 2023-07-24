package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class PostAttachment {
    private static final String CAPTION = "caption";
    private static final String DESCRIPTION = "description";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String URL = "url";
    @SerializedName("caption")
    public String caption;
    @SerializedName("description")
    public String description;
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("url")
    public String url;
}
