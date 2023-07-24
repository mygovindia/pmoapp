package com.sanskrit.pmo.network.mygov.callbacks;


public interface RecentPostsListener {
    void onError();

    void onPostsFetched(Object recentPosts);
}
