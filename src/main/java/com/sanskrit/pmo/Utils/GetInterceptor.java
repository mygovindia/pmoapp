package com.sanskrit.pmo.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

class GetInterceptor implements Interceptor {
    GetInterceptor() {
    }

    public Response intercept(Chain chain) {
        try {
            return chain.proceed(chain.request().newBuilder().addHeader("Content-Type", "application/json").addHeader("Accept", "application/json").build());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
