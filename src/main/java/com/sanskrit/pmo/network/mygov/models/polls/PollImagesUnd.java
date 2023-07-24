package com.sanskrit.pmo.network.mygov.models.polls;

import org.parceler.Parcel;

@Parcel
public class PollImagesUnd {
    public String filename;
    public String full_url;

    public String getFilename() {
        return this.filename;
    }

    public String getFull_url() {
        return this.full_url;
    }
}
