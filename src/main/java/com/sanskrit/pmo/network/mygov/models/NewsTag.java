package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class NewsTag implements Serializable {
    private static final String NAME = "name";
    private static final String SLUG = "slug";
    private static final String TERM_ID = "term_taxonomy_id";
    @SerializedName("name")
    public String mName;
    @SerializedName("slug")
    public String mSLug;
    @SerializedName("term_taxonomy_id")
    public String mTermId;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSLug() {
        return mSLug;
    }

    public void setmSLug(String mSLug) {
        this.mSLug = mSLug;
    }

    public String getmTermId() {
        return mTermId;
    }

    public void setmTermId(String mTermId) {
        this.mTermId = mTermId;
    }
}
