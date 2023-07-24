package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class RecentPosts {
    private static final String POSTS = "posts";
    @SerializedName("posts")
    public List<Post> posts;
}
