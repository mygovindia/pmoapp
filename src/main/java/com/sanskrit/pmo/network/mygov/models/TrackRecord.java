package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackRecord {
    private static final String CONTENT = "content";
    private static final String ITEMS = "Items";
    @SerializedName("content")
    public List<TrackRecordModel> mContent;
    @SerializedName("Items")
    public String mItems;

    public class TrackRecordModel {
        private static final String CONTENT = "content";
        private static final String DATE = "date";
        private static final String ID = "ID";
        private static final String IMAGE = "feature_image";
        private static final String TITLE = "title";
        @SerializedName("content")
        public String mContent;
        @SerializedName("date")
        public String mDate;
        @SerializedName("ID")
        public String mID;
        @SerializedName("feature_image")
        public String mImage;
        @SerializedName("title")
        public String mTitle;
    }
}
