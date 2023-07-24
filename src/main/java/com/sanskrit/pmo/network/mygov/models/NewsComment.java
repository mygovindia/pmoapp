package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class NewsComment {
    private static final String AUTHOR = "comment_author";
    private static final String DATE = "comment_date";
    private static final String ID = "comment_ID";
    private static final String PARENT = "comment_parent";
    private static final String TEXT = "comment_text";
    @SerializedName("comment_author")
    public String mAuthor;
    @SerializedName("comment_date")
    public String mDate;
    @SerializedName("comment_ID")
    public String mId;
    @SerializedName("comment_parent")
    public String mParent;
    @SerializedName("comment_text")
    public String mText;
}
