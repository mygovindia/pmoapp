package com.sanskrit.pmo.network.server;

import android.content.Context;
import android.util.Log;

import com.sanskrit.pmo.Models.MyPojo;
import com.sanskrit.pmo.Models.YotubeFeedModels.YotubeItemsFeed;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.callbacks.ResponseListener;
import com.sanskrit.pmo.network.server.callbacks.RequestTokenListener;
import com.sanskrit.pmo.network.server.callbacks.UserLoginListener;
import com.sanskrit.pmo.network.server.callbacks.UserSignupListener;
import com.sanskrit.pmo.network.server.models.FacebookFeed;
import com.sanskrit.pmo.network.server.models.MkbResponse;
import com.sanskrit.pmo.network.server.models.MkbStream;
import com.sanskrit.pmo.network.server.models.MkbVideoResponse;
import com.sanskrit.pmo.network.server.models.NotificationResponse;
import com.sanskrit.pmo.network.server.models.RequestToken;
import com.sanskrit.pmo.network.server.models.UserLogin;
import com.sanskrit.pmo.network.server.models.UserSignup;
import com.sanskrit.pmo.twitter.core.models.Tweet;
import com.sanskrit.pmo.utils.Utils;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SanskritClient {
    //public static final String BASE_API_URL = "http://164.100.94.136/teamsanskrit/backend";
    //public static final String BASE_API_URL = "http://164.100.94.136/teamsanskrit/backend";
    public static final String BASE_API_URL = "https://api.pmindia.gov.in/";
    private static SanskritClient sInstance;
    private static final Object sLock = new Object();
    private SanskritRestService mRestService;

    public static SanskritClient getInstance(Context context) {
        SanskritClient sanskritClient;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new SanskritClient();
                sInstance.mRestService = (SanskritRestService) RestServiceFactory.create(context, BASE_API_URL, SanskritRestService.class, false);
            }
            sanskritClient = sInstance;
        }
        return sanskritClient;
    }

    public static SanskritClient getInstance(Context context, GenericCallback listener) {
        SanskritClient sanskritClient;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new SanskritClient();
                sInstance.mRestService = (SanskritRestService) RestServiceFactory.create(context, BASE_API_URL, SanskritRestService.class, false, listener);
            }
            sanskritClient = sInstance;
        }
        return sanskritClient;
    }

    public void getRequestToken(String permaToken, final RequestTokenListener listener) {
        this.mRestService.getAccessToken(Utils.encodeString(permaToken), new Callback<RequestToken>() {
            public void success(RequestToken token, Response response) {
                listener.success(token);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void loginUser(String token, String email, String password, final UserLoginListener listener) {
        this.mRestService.loginUser(email, password, token, new Callback<UserLogin>() {
            public void success(UserLogin login, Response response) {
                listener.success(login);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void signUpUser(String email, String pass, String doa, String dob, String name, int language, boolean signupuser, String uuid, String gcm, int verified, String token, UserSignupListener listener) {
        final UserSignupListener userSignupListener = listener;
        this.mRestService.signUpUser(email, pass, doa, dob, name, language, signupuser, uuid, gcm, verified, token, new Callback<UserSignup>() {
            public void success(UserSignup token, Response response) {
                userSignupListener.success(token);
            }

            public void failure(RetrofitError error) {
                userSignupListener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getMKB(final ResponseListener listener) {
        this.mRestService.getMKB(new Callback<Object>() {
            public void success(Object baseResponse, Response response) {
                listener.success(response);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getMKBVideo(final GenericCallback listener) {
        this.mRestService.getMKBVideo(new Callback<MkbVideoResponse>() {
            public void success(MkbVideoResponse baseResponse, Response response) {
                listener.success(baseResponse);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getAllNotifications(String uuid, String page, final GenericCallback listener) {
        this.mRestService.getAllNotifications(uuid, page, new Callback<NotificationResponse>() {
            public void success(NotificationResponse baseResponse, Response response) {
                listener.success(baseResponse);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getProfileDOADOBFromEmail(String email, final GenericCallback listener) {
        this.mRestService.getProfileDOADOBFromEmail(email, System.currentTimeMillis() + "", new Callback<MyPojo>() {
            public void success(MyPojo baseResponse, Response response) {
                listener.success(baseResponse);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getProfileDOADOBFromMobile(String mobile, final GenericCallback listener) {
        this.mRestService.getProfileDOADOBFromMobile(mobile, System.currentTimeMillis() + "", new Callback<MyPojo>() {
            public void success(MyPojo baseResponse, Response response) {
                listener.success(baseResponse);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }


    public void getUpcomingEvents(final ResponseListener listener) {
        this.mRestService.getUpcomingEvents(new Callback<Object>() {
            public void success(Object baseResponse, Response response) {
                listener.success(response);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getPastEvents(final ResponseListener listener) {
        this.mRestService.getPastEvents(new Callback<Object>() {
            public void success(Object baseResponse, Response response) {
                listener.success(response);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getPmTrivia(final ResponseListener listener) {
        this.mRestService.getPMTrivia(new Callback<Object>() {
            public void success(Object baseResponse, Response response) {
                listener.success(response);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void setUserLocation(String token, String lat, String lon, final ResponseListener listener) {
        this.mRestService.setUserLocation(token, lat, lon, new Callback<Object>() {
            public void success(Object baseResponse, Response response) {
                listener.success(response);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void setNewDates(String token, String lat, String lng, String email, String mobileNumber, String doaa, String dobb, final ResponseListener listener) {
        String doa = Utils.urlEncodeString(doaa);
        String dob = Utils.urlEncodeString(dobb);

        Log.v("TOKEN", "TOKEN: " + token);
        Log.v("EMAIL", "EMAIL ID: " + email);
        Log.v("EMAIL", "EMAIL ID: " + mobileNumber);
        Log.v("DOB", "DOB: " + dobb);
        Log.v("DOA", "DOA: " + doaa);

        if (doa.equals("") || dob.equals("")) {
            if (!doa.equals("")) {
                //this.mRestService.setNewDOA(token, doa, new Callback<Object>() {
                this.mRestService.updateDoA(token, lat, lng, email, mobileNumber, doa, new Callback<Object>() {
                    public void success(Object baseResponse, Response response) {
                        listener.success(response);
                    }

                    public void failure(RetrofitError error) {
                        listener.failure();
                        error.printStackTrace();
                    }
                });
            }
            if (!dob.equals("")) {
                //this.mRestService.setNewDOB(token, dob, new Callback<Object>() {
                this.mRestService.updateDoB(token, lat, lng, email, mobileNumber, dob, new Callback<Object>() {
                    public void success(Object baseResponse, Response response) {
                        listener.success(response);
                    }

                    public void failure(RetrofitError error) {
                        listener.failure();
                        error.printStackTrace();
                    }
                });
            }
            if (doa.equals("") && dob.equals("")) {
                listener.failure();
                return;
            }
            return;
        }
        //this.mRestService.setNewdates(token, doa, dob, new Callback<Object>() {
        this.mRestService.updateDoADoB(token, lat, lng, email, mobileNumber, doa, dob, new Callback<Object>() {

            public void success(Object baseResponse, Response response) {
                listener.success(response);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void submitFeedback(String content, String email, String sUse, String sDesign, String sContent, String sInteractivity, String ip, final ResponseListener listener) {
        this.mRestService.submitFeedback(content, email, sContent, sUse, sDesign, sInteractivity, ip, new Callback<Object>() {
            public void success(Object baseResponse, Response response) {
                listener.success(response);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }


    public void registerGCMKey(String key, String token, final ResponseListener listener) {
        this.mRestService.registerGCMKey(Utils.urlEncodeString(key), Utils.urlEncodeString(token), new Callback<Object>() {
            public void success(Object baseResponse, Response response) {
                listener.success(response);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void pingGhostPush(String key, final ResponseListener listener) {
        this.mRestService.registerGCMKey(Utils.urlEncodeString(key), new Callback<Object>() {
            public void success(Object baseResponse, Response response) {
                listener.success(response);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getMkbLanguages(final GenericCallback listener) {
        this.mRestService.getMkbLanguages("all", new Callback<List<String>>() {
            public void success(List<String> languages, Response response) {
                listener.success(languages);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getMkbEpisodes(String page, String language, final GenericCallback listener) {
        this.mRestService.getMkbEpisodes(page, language, new Callback<MkbResponse>() {
            public void success(MkbResponse object, Response response) {
                listener.success(object);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getMkbStreamUrl(String trackId, final GenericCallback listener) {
        this.mRestService.getMkbStreamUrl(trackId, new Callback<MkbStream>() {
            public void success(MkbStream stream, Response response) {
                listener.success(stream);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getTwitterFeed(String language, String page, final GenericCallback listener) {
        this.mRestService.getTwitterFeed(language, page, new Callback<List<Tweet>>() {
            public void success(List<Tweet> tweets, Response response) {
                listener.success(tweets);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getFacebookFeed(String language, String page, final GenericCallback listener) {
        this.mRestService.getFacebookFeed(language, page, new Callback<List<FacebookFeed>>() {
            public void success(List<FacebookFeed> tweets, Response response) {
                listener.success(tweets);
            }
            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getYoutubeFeed(String language, String page, final GenericCallback listener) {
        this.mRestService.getYoutubeFeed(language, page, new Callback<YotubeItemsFeed>() {

            @Override
            public void success(YotubeItemsFeed youtubeFeeds, Response response) {
                listener.success(youtubeFeeds);
            }

            @Override
            public void failure(RetrofitError error) {
                listener.failure();
            }
        });
    }

    public void lookupTweets(String ids, final GenericCallback listener) {
        this.mRestService.lookupTweets(ids, new Callback<List<Tweet>>() {
            public void success(List<Tweet> tweets, Response response) {
                listener.success(tweets);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }
}
