package com.sanskrit.pmo.network.server;


import com.sanskrit.pmo.Models.MyPojo;
import com.sanskrit.pmo.Models.YotubeFeedModels.YotubeItemsFeed;
import com.sanskrit.pmo.network.server.models.FacebookFeed;
import com.sanskrit.pmo.network.server.models.MkbResponse;
import com.sanskrit.pmo.network.server.models.MkbStream;
import com.sanskrit.pmo.network.server.models.MkbVideoResponse;
import com.sanskrit.pmo.network.server.models.MyGovToken;
import com.sanskrit.pmo.network.server.models.NotificationResponse;
import com.sanskrit.pmo.network.server.models.RequestToken;
import com.sanskrit.pmo.network.server.models.UserLogin;
import com.sanskrit.pmo.network.server.models.UserProfileMygov;
import com.sanskrit.pmo.network.server.models.UserSignup;
import com.sanskrit.pmo.twitter.core.models.Tweet;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;


public interface SanskritRestService {
    public static final String EVENTS = "/get/events.php";
    public static final String EVENTS_PAST = "/get/events.php?past=true";
    public static final String FACEBOOK_FEED = "/homefeed/facebook.php";
    public static final String FEEDBACK = "/feedback.php";
    public static final String GET_USER_PROFILE = "/getUserProfile.php";
    public static final String GHOST_PUSH_PING = "/ghost_protocol.php";
    public static final String MAN_KI_BAAT = "/get/ManKiBaat.php";
    public static final String MAN_KI_BAAT_YOUTUBE = "/get/ManKiBaatYoutube.php";
    public static final String MKB_LANGUAGES = "/get/ManKiBaat.php";
    public static final String MYGOV_TOKEN = "/oauthflowredirect.php";
    public static final String NEW_DATES = "/verify/newDates.php";
    public static final String NOTIFICATIONS = "/get/notifications.php";
    public static final String REGISTER_GCM = "/gcm_unregistered.php";
    public static final String REQUEST_TOKEN = "/newToken.php";
    public static final String SOCIAL_FEED = "/homefeed/social.php";
    public static final String TRIVIA = "/get/pm_trivia.php";
    public static final String TWITTER_FEED = "/homefeed/twitter.php";
    public static final String USER_LOCATION = "/setUserLocation.php";
    public static final String USER_LOGIN = "/verify/authenticator.php";
    public static final String USER_SIGNUP = "/verify/credentials.php";

    @POST("/newToken.php")
    void getAccessToken(@Query("permaToken") String str, Callback<RequestToken> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/notifications/en/{page}")
    void getAllNotifications(@Query("user") String str, @Path("page") String str2, Callback<NotificationResponse> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/getprofiledetail/en/")
    void getProfileDOADOBFromEmail(@Query("email") String str, @Query("rand") String str1, Callback<MyPojo> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/getprofiledetail/en/")
    void getProfileDOADOBFromMobile(@Query("mobile") String str, @Query("rand") String str1, Callback<MyPojo> callback);


    /* @GET("/facebook/en")
     void getFacebookFeed(@Query("page") String str, Callback<List<FacebookFeed>> callback);*/
    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/facebook/{language}/{page}")
    void getFacebookFeed(@Path("language") String str, @Path("page") String str2, Callback<List<FacebookFeed>> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/youtube/{language}/{page}")
    void getYoutubeFeed(@Path("language") String str, @Path("page") String str2, Callback<YotubeItemsFeed> callback);

    @POST("/get/ManKiBaat.php")
    void getMKB(Callback<Object> callback);

    /*@POST("/get/ManKiBaatYoutube.php")
    void getMKBVideo(Callback<MkbVideoResponse> callback);*/
    /*@Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/mannkibaatyoutube/en/1")
    void getMKBVideo(Callback<MkbVideoResponse> callback);*/

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/mannkibaatyoutube/en")
    void getMKBVideo(Callback<MkbVideoResponse> callback);

    @GET("/get/ManKiBaat.php")
    void getMkbEpisodes(@Query("page") String str, @Query("language") String str2, Callback<MkbResponse> callback);

    @GET("/get/ManKiBaat.php")
    void getMkbLanguages(@Query("language") String str, Callback<List<String>> callback);

    @GET("/get/ManKiBaat.php")
    void getMkbStreamUrl(@Query("track_id") String str, Callback<MkbStream> callback);


    @GET("/oauthflowredirect.php")
    void getMyGovAccessToken(@Query("refresh_token") String str, Callback<MyGovToken> callback);

    @POST("/get/pm_trivia.php")
    void getPMTrivia(Callback<Object> callback);

    @POST("/get/events.php?past=true")
    void getPastEvents(Callback<Object> callback);

    @GET("/homefeed/social.php")
    void getSocialFeed(@Query("type") String str, @Query("page") String str2, Callback<Object> callback);

   /* @GET("/homefeed/twitter.php")
    void getTwitterFeed(@Query("page") String str, Callback<List<Tweet>> callback);*/

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @GET("/twitter/{language}/{page}")
    void getTwitterFeed(@Path("language") String str, @Path("page") String str2, Callback<List<Tweet>> callback);

    @POST("/get/events.php")
    void getUpcomingEvents(Callback<Object> callback);

    @POST("/getUserProfile.php")
    void getUserProfile(@Query("access_token") String str, Callback<UserProfileMygov> callback);

    @POST("/verify/authenticator.php")
    void loginUser(@Query("email") String str, @Query("password") String str2, @Query("token") String str3, Callback<UserLogin> callback);

    @GET("/homefeed/twitter.php")
    void lookupTweets(@Query("id") String str, Callback<List<Tweet>> callback);

    @POST("/gcm_unregistered.php")
    void registerGCMKey(@Query("gcm_key") String str, @Query("token") String str2, Callback<Object> callback);

    @GET("/ghost_protocol.php")
    void registerGCMKey(@Query("gcm_key") String str, Callback<Object> callback);

    @POST("/verify/newDates.php")
    void setNewDOA(@Query("token") String str, @Query("newDOA") String str2, Callback<Object> callback);

    @POST("/verify/newDates.php")
    void setNewDOB(@Query("token") String str, @Query("newDOB") String str2, Callback<Object> callback);

    @POST("/verify/newDates.php")
    void setNewdates(@Query("token") String str, @Query("newDOA") String str2, @Query("newDOB") String str3, Callback<Object> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @Multipart
    @POST("/newprofileupdate/")
    void updateDoA(@Part("token") String str, @Part("lat") String lat, @Part("lng") String lng, @Part("email") String str1, @Part("mobile") String str2, @Part("newDOA") String str3, Callback<Object> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @Multipart
    @POST("/newprofileupdate/")
    void updateDoB(@Part("token") String str, @Part("lat") String lat, @Part("lng") String lng, @Part("email") String str1, @Part("mobile") String str2, @Part("newDOB") String str3, Callback<Object> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @Multipart
    @POST("/newprofileupdate/")
    void updateDoADoB(@Part("token") String str, @Part("lat") String lat, @Part("lng") String lng, @Part("email") String str1, @Part("mobile") String str2, @Part("newDOA") String str3, @Part("newDOB") String str4, Callback<Object> callback);

    @POST("/setUserLocation.php")
    void setUserLocation(@Query("token") String str, @Query("latitude") String str2, @Query("longitude") String str3, Callback<Object> callback);

    @POST("/verify/credentials.php")
    void signUpUser(@Query("email") String str, @Query("password") String str2, @Query("DOA") String str3, @Query("DOB") String str4, @Query("full_name") String str5, @Query("langPref") int i, @Query("signupUser") boolean z, @Query("UUID") String str6, @Query("gcm") String str7, @Query("emailVerified") int i2, @Query("token") String str8, Callback<UserSignup> callback);

    @Headers({"Auth: 6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222"})
    @Multipart
    @POST("/feedback/en/1/")
    void submitFeedback(@Part("content") String str, @Part("email") String str2, @Part("star_content") String str3, @Part("star_use") String str4, @Part("star_design") String str5, @Part("star_interactivity") String str6, @Part("ip_address") String str7, Callback<Object> callback);
}
