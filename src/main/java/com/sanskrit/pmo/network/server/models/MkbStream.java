package com.sanskrit.pmo.network.server.models;

import org.parceler.Parcel;

@Parcel
public class MkbStream {
    String mp3_url;
    String track_id;

    public String getTrack_id() {
        return this.track_id;
    }

    public String getMp3_url() {
        return this.mp3_url;
    }
}
