package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class PostComment {
    private static final String AUTHOR = "name";
    private static final String DATE = "date";
    private static final String ID = "id";
    private static final String PARENT = "parent";
    private static final String TEXT = "content";
    @SerializedName("name")
    public String mAuthor;
    @SerializedName("date")
    public String mDate;
    @SerializedName("id")
    public String mId;
    @SerializedName("parent")
    public String mParent;
    @SerializedName("content")
    public String mText;
}
