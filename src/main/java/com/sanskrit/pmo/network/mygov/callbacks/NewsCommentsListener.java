package com.sanskrit.pmo.network.mygov.callbacks;

import com.sanskrit.pmo.network.mygov.models.NewsComment;

import java.util.List;

public interface NewsCommentsListener {
    void onCommnetsFetched(List<NewsComment> list);

    void onError();
}
