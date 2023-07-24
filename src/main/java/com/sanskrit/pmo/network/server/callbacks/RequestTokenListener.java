package com.sanskrit.pmo.network.server.callbacks;


import com.sanskrit.pmo.network.server.models.RequestToken;

public interface RequestTokenListener {
    void failure();

    void success(RequestToken requestToken);
}
