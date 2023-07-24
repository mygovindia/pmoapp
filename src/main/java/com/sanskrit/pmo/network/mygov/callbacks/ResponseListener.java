package com.sanskrit.pmo.network.mygov.callbacks;

import retrofit.client.Response;

public interface ResponseListener {
    void failure();

    void success(Response response);
}
