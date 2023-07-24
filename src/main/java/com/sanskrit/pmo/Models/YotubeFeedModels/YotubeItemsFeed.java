package com.sanskrit.pmo.Models.YotubeFeedModels;

public class YotubeItemsFeed {
    private Results[] results;

    private Pagging pagging;

    public Results[] getResults() {
        return results;
    }

    public void setResults(Results[] results) {
        this.results = results;
    }

    public Pagging getPagging() {
        return pagging;
    }

    public void setPagging(Pagging pagging) {
        this.pagging = pagging;
    }

    @Override
    public String toString() {
        return "ClassPojo [results = " + results + ", pagging = " + pagging + "]";
    }
}