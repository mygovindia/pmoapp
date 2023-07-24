package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PMQuotes {
    private static final String CONTENT = "content";
    private static final String ITEMS = "Items";
    @SerializedName("content")
    public List<PmQuotesModel> mContent;
    @SerializedName("Items")
    public String mItems;
}
