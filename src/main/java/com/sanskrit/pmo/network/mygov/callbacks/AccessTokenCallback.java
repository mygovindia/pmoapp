package com.sanskrit.pmo.network.mygov.callbacks;


import com.sanskrit.pmo.network.mygov.AccessToken;

public interface AccessTokenCallback {
    void tokenFailure();

    void tokenSuccess(AccessToken accessToken);
}
