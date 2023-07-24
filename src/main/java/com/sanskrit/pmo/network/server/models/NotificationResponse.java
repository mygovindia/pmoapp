package com.sanskrit.pmo.network.server.models;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class NotificationResponse {
    public int items;
    public int paging;
    public List<Notification> results;

    public List<Notification> getResults() {
        return this.results;
    }

    public int getItems() {
        return this.items;
    }

    public int getPaging() {
        return this.paging;
    }
}
