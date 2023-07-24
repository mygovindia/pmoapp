package com.sanskrit.pmo.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

class LogginInterceptor implements Interceptor {
    LogginInterceptor() {
    }

    public Response intercept(Chain chain) {
        try {
            return chain.proceed(chain.request().newBuilder().addHeader("Content-Type", "application/x-www-form-urlencoded").build());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
