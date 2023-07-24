package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

public class FormerPM {
    private static final String CONTENT = "content";
    private static final String IMAGE = "feature_image";
    private static final String LINK = "link";
    private static final String PARTY_NAME = "party-name";
    private static final String TENURE = "tenure";
    private static final String TITLE = "title";
    @SerializedName("content")
    public String mCOntent;
    @SerializedName("feature_image")
    public String mImage;
    @SerializedName("link")
    public String mLink;
    @SerializedName("party-name")
    public String mPartyName;
    @SerializedName("tenure")
    public String mTenure;
    @SerializedName("title")
    public String mTitle;
}
