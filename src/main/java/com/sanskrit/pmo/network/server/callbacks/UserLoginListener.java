package com.sanskrit.pmo.network.server.callbacks;


import com.sanskrit.pmo.network.server.models.UserLogin;

public interface UserLoginListener {
    void failure();

    void success(UserLogin userLogin);
}
