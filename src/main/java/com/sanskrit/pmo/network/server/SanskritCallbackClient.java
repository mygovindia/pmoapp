package com.sanskrit.pmo.network.server;

import android.content.Context;

import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.server.callbacks.UserProfileListener;
import com.sanskrit.pmo.network.server.models.MyGovToken;
import com.sanskrit.pmo.network.server.models.UserProfileMygov;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SanskritCallbackClient {
    public static final String BASE_API_URL = "https://auth.mygov.in";
    private static SanskritCallbackClient sInstance;
    private static final Object sLock = new Object();
    private SanskritRestService mRestService;

    public static SanskritCallbackClient getInstance(Context context) {
        SanskritCallbackClient sanskritCallbackClient;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new SanskritCallbackClient();
                sInstance.mRestService = (SanskritRestService) RestServiceFactory.create(context, BASE_API_URL, SanskritRestService.class, false);
            }
            sanskritCallbackClient = sInstance;
        }
        return sanskritCallbackClient;
    }

    public void getUserProfile(String token, final UserProfileListener listener) {
        this.mRestService.getUserProfile(token, new Callback<UserProfileMygov>() {
            public void success(UserProfileMygov profile, Response response) {
                listener.success(profile);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getMygovToken(String refreshToken, final GenericCallback listener) {
        this.mRestService.getMyGovAccessToken(refreshToken, new Callback<MyGovToken>() {
            public void success(MyGovToken myGovToken, Response response) {
                listener.success(myGovToken);
            }

            public void failure(RetrofitError error) {
                listener.failure();
            }
        });
    }
}
