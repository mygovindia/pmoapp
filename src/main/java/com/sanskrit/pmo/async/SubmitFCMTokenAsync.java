package com.sanskrit.pmo.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.CommonFunctions;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SubmitFCMTokenAsync extends AsyncTask<String, Void, String> {
    final String TAG = SubmitFCMTokenAsync.class.getSimpleName();
    final OkHttpClient client;
    private Context context;

    public SubmitFCMTokenAsync(Context context) {
        this.context = context;
        this.client = CommonFunctions.HtppcallforInterprator();
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {
        try {
            Log.v(TAG, "FCM ID: " + params[0]);
            Log.v(TAG, "IMEI: " + params[1]);
            FormBody.Builder add = new FormBody.Builder()
                    .add("email", "")
                    .add("mobile", "")
                    .add("notification", PreferencesUtility.getPrefNotificationOnOf(context))
                    .add("password", "")
                    .add("device_type", "android")
                    .add("device_token", params[0])
                    .add("imei", params[1]);
            Response execute = this.client.newCall(new Request.Builder()
                    .url("https://api.pmindia.gov.in/mobile_login_insert")
                    .addHeader("Auth", "6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222")
                    .post(add.build()).build()).execute();
            if (execute.isSuccessful()) {
                return execute.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null && result.trim().length() > 0 && ("Success Inserted".equalsIgnoreCase(getResult(result))
                || "Success Inserted!".equalsIgnoreCase(getResult(result))
                || "Success Updated".equalsIgnoreCase(getResult(result))
                || "Success Updated!".equalsIgnoreCase(getResult(result)))) {
            Log.v(TAG, "STATUS: DONE");
            PreferencesUtility.setFCMSubmitted(context, true);
        } else {
            Log.v(TAG, "STATUS: FAILED");
        }
    }

    private String getResult(String response) {
        if (response == null) return "";
        return response.substring(1, response.length() - 1);
    }
}