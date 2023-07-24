package com.sanskrit.pmo.network.mygov.callbacks;



import com.sanskrit.pmo.network.mygov.models.NewsFeed;

import java.util.List;

public interface NewsFeedListener {
    void failure();

    void success(List<NewsFeed> list);
}
