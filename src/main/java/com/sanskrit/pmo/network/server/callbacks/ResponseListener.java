package com.sanskrit.pmo.network.server.callbacks;

import retrofit.client.Response;

public interface ResponseListener {
    void failure();

    void success(Response response);
}
