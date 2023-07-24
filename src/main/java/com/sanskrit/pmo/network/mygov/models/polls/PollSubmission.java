package com.sanskrit.pmo.network.mygov.models.polls;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class PollSubmission {
    private static final String CHOICE = "choice";
    private static final String NID = "nid";
    @SerializedName("choice")
    public String choice;
    @SerializedName("nid")
    public String id;

    public void setId(String id) {
        this.id = id;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
