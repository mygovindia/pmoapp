package com.sanskrit.pmo.network.mygov;

import android.content.Context;
import android.widget.Toast;

import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.callbacks.RecentPostsListener;
import com.sanskrit.pmo.network.mygov.models.Post;
import com.sanskrit.pmo.network.mygov.models.polls.PollModel;
import com.sanskrit.pmo.network.mygov.models.polls.PollQuestionResponse;
import com.sanskrit.pmo.network.mygov.models.polls.PollSubmission;
import com.sanskrit.pmo.network.server.RestServiceFactory;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyGovBlogsClient {
    private static final String API_KEY = "57076294a5e2ab7fe000000151da1b87c5224e1a4d05c7d8553d2aa2";
    public static final String BASE_API_URL = "https://api.pmindia.gov.in";
    private static MyGovBlogsClient sInstance;
    private static final Object sLock = new Object();
    private MygovRestService mRestService;

    public static MyGovBlogsClient getInstance(Context context) {
        MyGovBlogsClient myGovBlogsClient;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new MyGovBlogsClient();
                sInstance.mRestService = (MygovRestService) RestServiceFactory.create(context, BASE_API_URL, MygovRestService.class, false);
            }
            myGovBlogsClient = sInstance;
        }
        return myGovBlogsClient;
    }

    public static MyGovBlogsClient getInstance(Context context, boolean useCache) {
        MyGovBlogsClient myGovBlogsClient;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new MyGovBlogsClient();
                sInstance.mRestService = (MygovRestService) RestServiceFactory.create(context, BASE_API_URL, MygovRestService.class, useCache);
            }
            myGovBlogsClient = sInstance;
        }
        return myGovBlogsClient;
    }

    public static MyGovBlogsClient getInstance(Context context, GenericCallback listener) {
        MyGovBlogsClient myGovBlogsClient;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new MyGovBlogsClient();
                sInstance.mRestService = (MygovRestService) RestServiceFactory.create(context, BASE_API_URL, MygovRestService.class, false, listener);
            }
            myGovBlogsClient = sInstance;
        }
        return myGovBlogsClient;
    }

    public void getInfographics(String languageCode, String page, final RecentPostsListener listener) {
        this.mRestService.getInfographics(languageCode, page, new Callback<List<Post>>() {

            public void success(List<Post> posts, Response response) {
                listener.onPostsFetched(posts);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
                listener.onError();
            }
        });
    }


    public void getPMQuotes(String languageCode, String page, final RecentPostsListener listener) {
        this.mRestService.getPMQuotes(languageCode, page, new Callback<List<Post>>() {

            public void success(List<Post> posts, Response response) {
                listener.onPostsFetched(posts);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
                listener.onError();
            }
        });
    }

    public void getAllPolls(int isPmoRelated, final GenericCallback listener) {
        this.mRestService.getAllPolls(API_KEY, isPmoRelated, new Callback<List<PollModel>>() {
            public void success(List<PollModel> polls, Response response) {
                listener.success(polls);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
                listener.failure();
            }
        });
    }

    public void getPollById(String id, final GenericCallback listener) {
        this.mRestService.getPollById(API_KEY, id, new Callback<PollModel>() {
            public void success(PollModel poll, Response response) {
                listener.success(poll);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
                listener.failure();
            }
        });
    }

    public void getPollQuestionById(String questionId, final GenericCallback listener) {
        this.mRestService.getPollQuestionById(API_KEY, questionId, new Callback<PollQuestionResponse>() {
            public void success(PollQuestionResponse questions, Response response) {
                listener.success(questions);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
                listener.failure();
            }
        });
    }

    public void submitPoll(final Context context, String token, PollSubmission submission) {
        this.mRestService.submitPoll(API_KEY, "OAuth2 " + token, submission, new Callback<Object>() {
            public void success(Object object, Response response) {
            }

            public void failure(RetrofitError error) {
                if (error.getResponse().getStatus() == 406 && context != null) {
                    Toast.makeText(context, "You have already submitted your opinion", 0).show();
                }
                error.printStackTrace();
            }
        });
    }
}
