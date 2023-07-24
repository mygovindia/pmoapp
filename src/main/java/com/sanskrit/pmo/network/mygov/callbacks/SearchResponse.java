package com.sanskrit.pmo.network.mygov.callbacks;

import com.sanskrit.pmo.network.mygov.models.SearchModel;

import java.util.List;

public class SearchResponse {
    public String Items;

    public List<SearchModel> content;

    public String getItems() {
        return this.Items;
    }

    public List<SearchModel> getContent() {
        return this.content;
    }
}
