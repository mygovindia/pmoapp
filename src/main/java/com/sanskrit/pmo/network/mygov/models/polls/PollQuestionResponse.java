package com.sanskrit.pmo.network.mygov.models.polls;

import org.parceler.Parcel;

import java.util.HashMap;

@Parcel
public class PollQuestionResponse {
    public HashMap<String, PollChoice> choice;
    public String nid;
    public String title;

    public String getTitle() {
        return this.title;
    }

    public String getNid() {
        return this.nid;
    }

    public HashMap<String, PollChoice> getChoice() {
        return this.choice;
    }
}
