package com.sanskrit.pmo.network.server.callbacks;


import com.sanskrit.pmo.network.server.models.UserSignup;

public interface UserSignupListener {
    void failure();

    void success(UserSignup userSignup);
}
