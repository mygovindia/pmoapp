package com.sanskrit.pmo.network.mygov;

import android.content.Context;

import com.sanskrit.pmo.network.mygov.callbacks.FormerPMListener;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.callbacks.NewsCommentsListener;
import com.sanskrit.pmo.network.mygov.callbacks.NewsFeedListener;
import com.sanskrit.pmo.network.mygov.callbacks.ResponseListener;
import com.sanskrit.pmo.network.mygov.callbacks.SearchMKBResponse;
import com.sanskrit.pmo.network.mygov.callbacks.SearchResponse;
import com.sanskrit.pmo.network.mygov.models.FormerPM;
import com.sanskrit.pmo.network.mygov.models.FunctionalChart;
import com.sanskrit.pmo.network.mygov.models.InsertNewComment;
import com.sanskrit.pmo.network.mygov.models.KnowPM;
import com.sanskrit.pmo.network.mygov.models.MediaCoverageDetails;
import com.sanskrit.pmo.network.mygov.models.NewsComment;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.network.mygov.models.PMQuotes;
import com.sanskrit.pmo.network.mygov.models.TrackRecord;
import com.sanskrit.pmo.network.mygov.models.gallery.ImageGallery;
import com.sanskrit.pmo.network.server.RestServiceFactory;
import com.sanskrit.pmo.network.server.models.MkbAudio;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MygovClient {
    public static final String BASE_API_URL = "https://api.pmindia.gov.in";
    private static MygovClient sInstance;
    private static final Object sLock = new Object();
    private MygovRestService mRestService;

    public static MygovClient getInstance(Context context) {
        MygovClient mygovClient;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new MygovClient();
                sInstance.mRestService = (MygovRestService) RestServiceFactory.create(context, "https://api.pmindia.gov.in", MygovRestService.class, false);
            }
            mygovClient = sInstance;
        }
        return mygovClient;
    }

    public static MygovClient getInstance(Context context, GenericCallback listener) {
        MygovClient mygovClient;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new MygovClient();
                sInstance.mRestService = (MygovRestService) RestServiceFactory.create(context, "https://api.pmindia.gov.in", MygovRestService.class, false, listener);
            }
            mygovClient = sInstance;
        }
        return mygovClient;
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

    public void getNewsDetail(String languageCode, String id, final NewsFeedListener listener) {
        this.mRestService.getNewsDetail(languageCode, id, new Callback<List<NewsFeed>>() {
            public void success(List<NewsFeed> record, Response response) {
                listener.success(record);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getGallery(String languageCode, String page, final GenericCallback listener) {
        this.mRestService.getGallery(languageCode, page, new Callback<ImageGallery>() {
            public void success(ImageGallery gallery, Response response) {
                listener.success(gallery);
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

    public void getLatestNews(String languageCode, String page, final NewsFeedListener listener) {
        this.mRestService.getLatestNews(languageCode, page, new Callback<List<NewsFeed>>() {
            public void success(List<NewsFeed> feeds, Response response) {
                listener.success(feeds);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getLatestMediaCoverage(String languageCode, String page, final GenericCallback listener) {
        this.mRestService.getLatestMediaCoverage(languageCode, page, new Callback<MediaCoverageDetails>() {
            public void success(MediaCoverageDetails feeds, Response response) {
                listener.success(feeds);
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

    public void getNewsComments(String languageCode, String newsId, final NewsCommentsListener listener) {
        this.mRestService.getNewsComments(languageCode, newsId, new Callback<List<NewsComment>>() {
            public void success(List<NewsComment> feeds, Response response) {
                listener.onCommnetsFetched(feeds);
            }

            public void failure(RetrofitError error) {
                listener.onError();
                error.printStackTrace();
            }
        });
    }

    public void postNewNewsComment(InsertNewComment comment, final ResponseListener listener) {
        this.mRestService.postNewNewsComment(comment.date, comment.id, comment.commentId, comment.name, comment.email, comment.comment, comment.ip, new Callback<Object>() {
            public void success(Object object, Response response) {
                listener.success(response);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void getTagWiseNews(String language, String page, String tags, final NewsFeedListener listener) {
        this.mRestService.getTagWiseNews(language, page, tags, new Callback<List<NewsFeed>>() {
            public void success(List<NewsFeed> feeds, Response response) {
                listener.success(feeds);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }

    public void search(String query, String languageCode, String page, final GenericCallback listener) {
        this.mRestService.search(query, languageCode, page, new Callback<SearchResponse>() {
            public void success(SearchResponse o, Response response) {
                listener.success(o);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void searchInFoGraphics(String query, String languageCode, String page, final GenericCallback listener) {
        this.mRestService.searchInFoGraphics(query, languageCode, page, new Callback<SearchResponse>() {
            public void success(SearchResponse o, Response response) {
                listener.success(o);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void searchQuotes(String query, String languageCode, String page, final GenericCallback listener) {
        this.mRestService.searchQuotes(query, languageCode, page, new Callback<SearchResponse>() {
            public void success(SearchResponse o, Response response) {
                listener.success(o);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void searchFormerPM(String query, String languageCode, String page, final GenericCallback listener) {
        this.mRestService.searchFormerPM(query, languageCode, page, new Callback<SearchResponse>() {
            public void success(SearchResponse o, Response response) {
                listener.success(o);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }


    public void searchTrackRecord(String query, String languageCode, String page, final GenericCallback listener) {
        this.mRestService.searchTrackRecord(query, languageCode, page, new Callback<SearchResponse>() {
            public void success(SearchResponse o, Response response) {
                listener.success(o);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void searchGallery(String query, String languageCode, String page, final GenericCallback listener) {
        this.mRestService.searchGallery(query, languageCode, page, new Callback<SearchResponse>() {
            public void success(SearchResponse o, Response response) {
                listener.success(o);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void searchMKB(String query, String languageCode, String page, final GenericCallback listener) {
        this.mRestService.searchMKB(query, languageCode, page, new Callback<SearchMKBResponse>() {
            public void success(SearchMKBResponse o, Response response) {
                listener.success(o);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }


    public void getMkbEpisodes(String language, String page, final GenericCallback listener) {
        this.mRestService.getMKBEpisodes(language, page, new Callback<List<MkbAudio>>() {
            public void success(List<MkbAudio> feeds, Response response) {
                listener.success(feeds);
            }

            public void failure(RetrofitError error) {
                listener.failure();
                error.printStackTrace();
            }
        });
    }
}
