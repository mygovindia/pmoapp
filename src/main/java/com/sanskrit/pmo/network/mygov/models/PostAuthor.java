package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class PostAuthor {
    private static final String DESCRIPTION = "description";
    private static final String ID = "id";
    private static final String NAME = "name";
    @SerializedName("description")
    public String description;
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
}
