package com.sanskrit.pmo.network.mygov;


import com.sanskrit.pmo.network.mygov.callbacks.SearchMKBResponse;
import com.sanskrit.pmo.network.mygov.callbacks.SearchResponse;
import com.sanskrit.pmo.network.mygov.models.FormerPM;
import com.sanskrit.pmo.network.mygov.models.FunctionalChart;
import com.sanskrit.pmo.network.mygov.models.KnowPM;
import com.sanskrit.pmo.network.mygov.models.MediaCoverageDetails;
import com.sanskrit.pmo.network.mygov.models.MkbSuggestion;
import com.sanskrit.pmo.network.mygov.models.NewsComment;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.network.mygov.models.PMQuotes;
import com.sanskrit.pmo.network.mygov.models.Post;
import com.sanskrit.pmo.network.mygov.models.TrackRecord;
import com.sanskrit.pmo.network.mygov.models.gallery.ImageGallery;
import com.sanskrit.pmo.network.mygov.models.polls.PollModel;
import com.sanskrit.pmo.network.mygov.models.polls.PollQuestionResponse;
import com.sanskrit.pmo.network.mygov.models.polls.PollSubmission;
import com.sanskrit.pmo.network.server.models.MkbAudio;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;


public interface MygovRestService {
    public static final String BASE_PARAMETERS_ACCESS_TOKEN = "/token?";
    public static final String FORMER_PMS = "/former-pms/{language}";
    public static final String GALLERY = "/gallery/{language}/{page}";
    public static final String GOVERNANCE_TRACK_RECORD = "/governance-track-record/{language}/{page}";
    public static final String INFOGRAPHICS = "/infographics/{language}/{page}/";
    public static final String KNOW_YOUR_PM = "/pm-profile/{language}";
    public static final String MANN_KI_BAAT = "/mann-ki-baat/{language}/{page}";
    public static final String MKB_SUGGESTION = "/MkbDetails/mservice";
    public static final String NEWS = "/latest-news/{language}/{items}";
    public static final String NEWS_COMMENTS = "/news_list_comment/{language}/{id}";
    public static final String NEWS_DETAIL = "/news_detail_with_comment/{language}/{id}";
    public static final String NEWS_INSERT_COMMENT = "/news_insert_comment/";
    public static final String PMO_FUNCTIONAL_CHART = "/pmo-functional-chart/{language}";
    public static final String PM_QUOTES = "/quotes/{language}/{page}";
    public static final String POLLS = "/poll/";
    public static final String POLLY_BY_ID = "/poll/{id}/";
    public static final String POLL_QUESTION_BY_ID = "/poll-question/{id}/";
    public static final String POLL_SUBMIT = "/poll-submission/";
    public static final String SEARCH = "/search/{language}/";
    public static final String TAG_WISE_NEWS = "/tagnewsbyname/{language}/{page}/";

    @Headers({"Accept: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("/token?")
    void getAccessToken(@Field("client_id") String str, @Field("code") String str2, @Field("client_secret") String str3, @Field("scope") String str4, @Field("grant_type") String str5, @Field("redirect_uri") String str6, Callback<AccessToken> callback);

    @GET("/poll/")
    void getAllPolls(@Query("api_key") String str, @Query("parameters[field_is_feature]") int i, Callback<List<PollModel>> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/former-pms/{language}")
    void getFormerPMs(@Path("language") String str, Callback<List<FormerPM>> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/gallery/{language}/{page}")
    void getGallery(@Path("language") String str, @Path("page") String str2, Callback<ImageGallery> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/infographics/{language}/{page}/")
    void getInfographics(@Path("language") String str, @Path("page") String str2, Callback<List<Post>> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/photoQuotes/{language}/{page}/")
    void getPMQuotes(@Path("language") String str, @Path("page") String str2, Callback<List<Post>> callback);


    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/latest-news/{language}/{items}")
    void getLatestNews(@Path("language") String str, @Path("items") String str2, Callback<List<NewsFeed>> callback);
/*

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/media-coverage/{language}/{items}")
    void getLatestMediaCoverage(@Path("language") String str, @Path("items") String str2, Callback<MediaCoverageModel> callback);
*/

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/mann-ki-baat/{language}/{page}")
    void getMKBEpisodes(@Path("language") String str, @Path("page") String str2, Callback<List<MkbAudio>> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/media-coverage/{language}/{page}")
    void getLatestMediaCoverage(@Path("language") String str, @Path("page") String str2, Callback<MediaCoverageDetails> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/news_list_comment/{language}/{id}")
    void getNewsComments(@Path("language") String str, @Path("id") String str2, Callback<List<NewsComment>> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/news_detail_with_comment/{language}/{id}")
    void getNewsDetail(@Path("language") String str, @Path("id") String str2, Callback<List<NewsFeed>> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/pmo-functional-chart/{language}")
    void getPMOFunctionalChart(@Path("language") String str, Callback<FunctionalChart> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/privacy-policy/{language}")
    void getPrivacyPolicy(@Path("language") String str, Callback<List<PrivacyPolicy>> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/pm-profile/{language}")
    void getPMProfile(@Path("language") String str, Callback<KnowPM> callback);

    @GET("/poll/{id}/")
    void getPollById(@Query("api_key") String str, @Path("id") String str2, Callback<PollModel> callback);

    @GET("/poll-question/{id}/")
    void getPollQuestionById(@Query("api_key") String str, @Path("id") String str2, Callback<PollQuestionResponse> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/quotes/{language}/{page}")
    void getQuotes(@Path("language") String str, @Path("page") String str2, Callback<PMQuotes> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/tagnewsbyname/{language}/{page}/")
    void getTagWiseNews(@Path("language") String str, @Path("page") String str2, @Query("fields") String str3, Callback<List<NewsFeed>> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/governance-track-record/{language}/{page}")
    void getTrackRecord(@Path("language") String str, @Path("page") String str2, Callback<TrackRecord> callback);


    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @Multipart
    @POST("/news_insert_comment/")
    void postNewNewsComment(@Part("date") String str, @Part("news_post_id") String str2, @Part("parent_comment_id") String str3, @Part("author_name") String str4, @Part("comment_author_email") String str5, @Part("author_comment") String str6, @Part("comment_author_IP") String str7, Callback<Object> callback);


    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/search/{language}/{page}")
    void search(@Query("s") String str, @Path("language") String str2, @Path("page") String str3, Callback<SearchResponse> callback);


    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/photos-infographics-search/{language}/{page}")
    void searchInFoGraphics(@Query("s") String str, @Path("language") String str2, @Path("page") String str3, Callback<SearchResponse> callback);


    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/photo-quotes-search/{language}/{page}")
    void searchQuotes(@Query("s") String str, @Path("language") String str2, @Path("page") String str3, Callback<SearchResponse> callback);


    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/former-pm-gallery-search/{language}/{page}")
    void searchFormerPM(@Query("s") String str, @Path("language") String str2, @Path("page") String str3, Callback<SearchResponse> callback);


    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/governance-track-record-search/{language}/{page}")
    void searchTrackRecord(@Query("s") String str, @Path("language") String str2, @Path("page") String str3, Callback<SearchResponse> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/allphotos-gallery-search/{language}/{page}")
    void searchGallery(@Query("s") String str, @Path("language") String str2, @Path("page") String str3, Callback<SearchResponse> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/mann-ki-baat-search/{language}/{page}")
    void searchMKB(@Query("s") String str, @Path("language") String str2, @Path("page") String str3, Callback<SearchMKBResponse> callback);


    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222", "Content-type: application/json"})
    @POST("/MkbDetails/mservice")
    void submitMkbSuggestion(@Body MkbSuggestion mkbSuggestion, Callback<Object> callback);

    @POST("/poll-submission/")
    void submitPoll(@Query("api_key") String str, @Header("Authorization") String str2, @Body PollSubmission pollSubmission, Callback<Object> callback);
}
