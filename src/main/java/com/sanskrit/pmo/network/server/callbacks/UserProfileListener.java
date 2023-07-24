package com.sanskrit.pmo.network.server.callbacks;


import com.sanskrit.pmo.network.server.models.UserProfileMygov;

public interface UserProfileListener {
    void failure();

    void success(UserProfileMygov userProfileMygov);
}
