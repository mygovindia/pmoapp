package com.sanskrit.pmo.network.mygov;

import android.content.Context;

import com.sanskrit.pmo.network.mygov.callbacks.FormerPMListener;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.callbacks.ResponseListener;
import com.sanskrit.pmo.network.mygov.models.FormerPM;
import com.sanskrit.pmo.network.mygov.models.FunctionalChart;
import com.sanskrit.pmo.network.mygov.models.KnowPM;
import com.sanskrit.pmo.network.mygov.models.PMQuotes;
import com.sanskrit.pmo.network.mygov.models.TrackRecord;
import com.sanskrit.pmo.network.mygov.models.gallery.ImageGallery;
import com.sanskrit.pmo.network.server.RestServiceFactory;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyGovCacheClient {
    public static final String BASE_API_URL = "https://api.pmindia.gov.in";
    private static MyGovCacheClient sInstance;
    private static final Object sLock = new Object();
    private MygovRestService mRestService;

    public static MyGovCacheClient getInstance(Context context) {
        MyGovCacheClient myGovCacheClient;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new MyGovCacheClient();
                sInstance.mRestService = (MygovRestService) RestServiceFactory.create(context, "https://api.pmindia.gov.in", MygovRestService.class, true);
            }
            myGovCacheClient = sInstance;
        }
        return myGovCacheClient;
    }

    public void getPMProfile(String languageCode, final GenericCallback listener) {
        this.mRestService.getPMProfile(languageCode, new Callback<KnowPM>() {
            public void success(KnowPM pm, Response response) {
                listener.success(pm);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getFormerPMs(String languageCode, final FormerPMListener listener) {
        this.mRestService.getFormerPMs(languageCode, new Callback<List<FormerPM>>() {
            public void success(List<FormerPM> formerPMs, Response response) {
                listener.success(formerPMs);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getTrackRecord(String languageCode, String page, final GenericCallback listener) {
        this.mRestService.getTrackRecord(languageCode, page, new Callback<TrackRecord>() {
            public void success(TrackRecord record, Response response) {
                listener.success(record);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getGallery(String languageCode, String page, final ResponseListener listener) {
        this.mRestService.getGallery(languageCode, page, new Callback<ImageGallery>() {
            public void success(ImageGallery gallery, Response response) {
                listener.success(response);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getPMOFunctionalChart(String languageCode, final GenericCallback listener) {
        this.mRestService.getPMOFunctionalChart(languageCode, new Callback<FunctionalChart>() {
            public void success(FunctionalChart chart, Response response) {
                listener.success(chart);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getPrivacyPolicy(String languageCode, final GenericCallback listener) {
        this.mRestService.getPrivacyPolicy(languageCode, new Callback<List<PrivacyPolicy>>() {
            public void success(List<PrivacyPolicy> privacyPolicy, Response response) {
                listener.success(privacyPolicy);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getQuotes(String languageCode, String page, final GenericCallback listener) {
        this.mRestService.getQuotes(languageCode, page, new Callback<PMQuotes>() {
            public void success(PMQuotes quotes, Response response) {
                listener.success(quotes);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }
}
