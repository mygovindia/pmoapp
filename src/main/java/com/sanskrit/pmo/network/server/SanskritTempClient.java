package com.sanskrit.pmo.network.server;

import android.content.Context;

import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SanskritTempClient {
    public static final String BASE_API_URL = "https://api.pmindia.gov.in";
    private static SanskritTempClient sInstance;
    private static final Object sLock = new Object();
    private SanskritRestService mRestService;
    private static GenericCallback listener;

    class C12271 implements Callback<Object> {
        C12271() {
        }

        public void success(Object token, Response response) {
            listener.success(token);
        }

        public void failure(RetrofitError error) {
            listener.failure();
            error.printStackTrace();
        }
    }

    public static SanskritTempClient getInstance(Context context) {
        SanskritTempClient sanskritTempClient;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new SanskritTempClient();
                sInstance.mRestService = (SanskritRestService) RestServiceFactory.create(context, BASE_API_URL, SanskritRestService.class, false);
            }
            sanskritTempClient = sInstance;
        }
        return sanskritTempClient;
    }

    public static SanskritTempClient getInstance(Context context, GenericCallback listener) {
        SanskritTempClient sanskritTempClient;
        SanskritTempClient.listener = listener;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new SanskritTempClient();
                sInstance.mRestService = (SanskritRestService) RestServiceFactory.create(context, BASE_API_URL, SanskritRestService.class, false, listener);
            }
            sanskritTempClient = sInstance;
        }
        return sanskritTempClient;
    }

    public void getSocialFeed(String types, String page) {
        this.mRestService.getSocialFeed(types, page, new C12271());
    }

    /*public void getYoutubeFeed(String language, String page) {
        this.mRestService.getYoutubeFeed(language, page, new C12271());
    }*/
}
