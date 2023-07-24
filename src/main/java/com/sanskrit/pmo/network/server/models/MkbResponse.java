package com.sanskrit.pmo.network.server.models;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class MkbResponse {
    List<MkbAudio> results;

    public List<MkbAudio> getResults() {
        return this.results;
    }
}
