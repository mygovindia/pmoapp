package com.sanskrit.pmo.network.server.models;

import org.parceler.Parcel;

@Parcel
public class Notification {
    public String extra_id;
    public String id;
    public String link;
    public String push;
    public String time;
    public String type;

    public String getPush() {
        return this.push;
    }

    public String getLink() {
        return this.link;
    }

    public String getType() {
        return this.type;
    }

    public String getTime() {
        return this.time;
    }

    public String getExtra_id() {
        return this.extra_id;
    }

    public String getId() {
        return this.id;
    }
}
