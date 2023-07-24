package com.sanskrit.pmo.Session;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class PreferencesUtility {
    private static final String TAG = "PreferencesUtility";
    private static final String AUTH_COOKIES = "auth_cookies";
    private static final String BACKGROUND_CACHE = "toggle_cache";
    private static final String FIRST_RUN = "first_run";
    private static final String IS_FCM_SUBMITTED = "IS_FCM_SUBMITTED";
    private static final String FONT_SIZE = "font_size";
    private static final String GCM_REGISTERED = "gcm_registered";
    private static final String KEY_VALUE = "com.sanskrit.pmo";
    private static final String LANGUAGE = "language";
    static final String LAST_LOCATION_TIME = "last_location_time";
    private static final String LOCATION_PUSH_NOTIFICATIONS = "toggle_location_push";
    private static final String MKB_CURRENT_LANGUAGE = "mkb_current_language";
    private static final String MKB_CURRENT_LANGUAGE_CODE = "mkb_current_language_code";
    private static final String MKB_CURRENT_TRACK = "mkb_current_track";
    private static final String MKB_CURRENT_TRACKDATE = "mkb_current_track_date";
    private static final String MKB_CURRENT_TRACK_ID = "mkb_current_track_id";
    private static final String MKB_CURRENT_TRACK_IMAGE = "mkb_current_track_image";
    private static final String MKB_CURRENT_TRACK_URL = "mkb_current_track_url";
    private static final String MKB_FILTER = "mkb_filter";
    static String MYGOV_AUTH_PREFERENCE = "mygov_oauth";
    static final String MYGOV_OAUTH_EMAIL = "oauth_email";
    static final String MYGOV_OAUTH_NAME = "oauth_name";
    static final String MYGOV_OAUTH_REFRESH_TOKEN = "oauth_refresh_token";
    static final String MYGOV_OAUTH_TOKEN = "oauth_token";
    static final String PREF_MYGOV_LOGIN = "isMygovLogedIn";
    static final String PREF_NOTIFICATION_ON_OF = "PREF_NOTIFICATION_ON_OF";
    static final String PREF_MOBILE_NUMBER = "PREF_MOBILE_NUMBER";

    static final String PREF_TWITTER_LOGIN = "isTwitterLogedIn";
    private static final String PROFILE_FIRST_RUN = "profile_first_run";
    private static final String PUSH_FILTER = "push_filter";
    private static final String PUSH_NOTIFICATIONS = "toggle_push";
    static final String SERVER_GCM_key = "gcm_key";
    static String SERVER_PREFERENCE = "server_pref";
    static final String SERVER_USER_DOA = "server_user_doa";
    static final String SERVER_USER_DOB = "server_user_dob";
    static final String SERVER_UUID = "server_uuid";
    static final String FCM_ID = "fcm_id";
    static final String DEVICE_IMEI = "device_imei";
    private static final String SOCIAL_FEED_FILTER_TWITTER = "feed_filter_twitter";
    private static final String SOCIAL_FEED_FILTER_YOUTUBE = "feed_filter_youtube";
    private static final String SOCIAL_FEED_PREFERNCE = "social_feed_preference";
    private static final String THEME_PREFERNCE = "theme_preference";
    static String TWITTER_AUTH_PREFERENCE = "twitter_oauth";
    static final String TWITTER_OAUTH_SECRET = "oauth_token_secret";
    static final String TWITTER_OAUTH_TOKEN = "oauth_token";
    static final String UNIQUE_DEVICE_ID = "UNIQUE_DEVICE_ID";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("com.app.pmo", 0);
    }


    private static SharedPreferences getDefaultSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences getTwitterAuthPreferences(Context context) {
        return context.getSharedPreferences(TWITTER_AUTH_PREFERENCE, 0);
    }

    private static SharedPreferences getMygovAuthPreferences(Context context) {
        return context.getSharedPreferences(MYGOV_AUTH_PREFERENCE, 0);
    }

    private static SharedPreferences getServerPreferences(Context context) {
        return context.getSharedPreferences(SERVER_PREFERENCE, 0);
    }

    public static void setOnSharedPreferenceChangeListener(Context context, OnSharedPreferenceChangeListener listener) {
        getSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener);
    }

    public static void storeCookieId(Context context, String authCookies) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(AUTH_COOKIES, authCookies);
        editor.commit();
    }

    public static void setLanguagePreference(Context context, String language) {
        Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putString(LANGUAGE, language);
        editor.commit();
    }

    public static String getLanguagePrefernce(Context context) {
        return getDefaultSharedPreferences(context).getString(LANGUAGE, "en");
    }

    public static String getSocialFeedPreferncee(Context context) {
        return getSharedPreferences(context).getString(SOCIAL_FEED_PREFERNCE, "twitter");
    }

    public static void setSocialFeedPreferncee(Context context, String preferncee) {
        Editor preferences = getSharedPreferences(context).edit();
        preferences.putString(SOCIAL_FEED_PREFERNCE, preferncee);
        preferences.commit();
    }


    public static void setUniqueDeviceId(Context context, String uniqueDeviceId) {
        Editor preferences = getSharedPreferences(context).edit();
        preferences.putString(UNIQUE_DEVICE_ID, uniqueDeviceId);
        preferences.commit();
        Log.d(TAG, "setUniqueDeviceId: "+uniqueDeviceId);
    }


    public static String getUniqueDeviceId(Context context) {
        return getSharedPreferences(context).getString(UNIQUE_DEVICE_ID,"");
    }

    public static boolean getLocationPushesEnabled(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(LOCATION_PUSH_NOTIFICATIONS, true);
    }

    public static void setCurrentMKBTrackName(Context context, String name) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(MKB_CURRENT_TRACK, name);
        editor.commit();
    }

    public static void setCurrentMKBTrackDate(Context context, String date) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(MKB_CURRENT_TRACKDATE, date);
        editor.commit();
    }

    public static void setCurrentMKBTrackImage(Context context, String image) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(MKB_CURRENT_TRACK_IMAGE, image);
        editor.commit();
    }

    public static void setMkbLanguageName(Context context, String lang) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(MKB_CURRENT_LANGUAGE, lang);
        editor.commit();
    }

    public static void setMkbLanguageCode(Context context, String lang) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(MKB_CURRENT_LANGUAGE_CODE, lang);
        editor.commit();
    }

    public static void setCurrentMKBTrackURL(Context context, String url) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(MKB_CURRENT_TRACK_URL, url);
        editor.commit();
    }

    public static void setCurrentMKBTrackID(Context context, String id) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(MKB_CURRENT_TRACK_ID, id);
        editor.commit();
    }

    public static String getMkbCurrentTrack(Context context) {
        return getSharedPreferences(context).getString(MKB_CURRENT_TRACK, null);
    }

    public static String getMkbCurrentTrackImage(Context context) {
        return getSharedPreferences(context).getString(MKB_CURRENT_TRACK_IMAGE, null);
    }

    public static String getMkbCurrentTrackdate(Context context) {
        return getSharedPreferences(context).getString(MKB_CURRENT_TRACKDATE, null);
    }

    public static String getMkbLanguageName(Context context) {
        return getSharedPreferences(context).getString(MKB_CURRENT_LANGUAGE, "English");
    }

    public static String getMkbLanguageCode(Context context) {
        return getSharedPreferences(context).getString(MKB_CURRENT_LANGUAGE_CODE, "en");
    }

    public static String getMkbCurrentTrackUrl(Context context) {
        return getSharedPreferences(context).getString(MKB_CURRENT_TRACK_URL, null);
    }

    public static String getMkbCurrentTrackId(Context context) {
        return getSharedPreferences(context).getString(MKB_CURRENT_TRACK_ID, null);
    }

    public static String getTheme(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(THEME_PREFERNCE, "light");
    }

    public static void setTwitterAuthToken(Context context, String token, String secret, boolean loggedIn) {
        Editor editor = getTwitterAuthPreferences(context).edit();
        editor.putString("oauth_token", token);
        editor.putString("oauth_token_secret", secret);
        editor.putBoolean(PREF_TWITTER_LOGIN, loggedIn);
        editor.commit();
    }

    public static void clearTwitterCredentials(Context context) {
        Editor e = getTwitterAuthPreferences(context).edit();
        e.remove("oauth_token");
        e.remove("oauth_token_secret");
        e.remove(PREF_TWITTER_LOGIN);
        e.commit();
    }

    public static String getTwitterOauthToken(Context context) {
        return getTwitterAuthPreferences(context).getString("oauth_token", null);
    }

    public static String getTwitterOauthSecret(Context context) {
        return getTwitterAuthPreferences(context).getString("oauth_token_secret", null);
    }

    public static boolean getTwitterLoggedIn(Context context) {
        return getTwitterAuthPreferences(context).getBoolean(PREF_TWITTER_LOGIN, false);
    }

    public static void setTwiiterFilterEnabled(Context context, boolean b) {
        Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putBoolean(SOCIAL_FEED_FILTER_TWITTER, b);
        editor.commit();
    }

    public static boolean getTwitterFilterEnabled(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(SOCIAL_FEED_FILTER_TWITTER, true);
    }

    public static void setYoutubeFilterEnabled(Context context, boolean b) {
        Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putBoolean(SOCIAL_FEED_FILTER_YOUTUBE, b);
        editor.commit();
    }

    public static boolean getYoutubeFilterEnabled(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(SOCIAL_FEED_FILTER_YOUTUBE, false);
    }

    public static void setMKBFilter(Context context, String s) {
        Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putString(MKB_FILTER, s);
        editor.apply();
    }

    public static String getMKBFilter(Context context) {
        return getDefaultSharedPreferences(context).getString(MKB_FILTER, "audio");
    }

    public static void setPushFilter(Context context, String s) {
        Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putString(PUSH_FILTER, s);
        editor.apply();
    }

    public static String getPushFilter(Context context) {
        return getDefaultSharedPreferences(context).getString(PUSH_FILTER, "all");
    }

    public static void setBackgroundCacheEnabled(Context context, boolean b) {
        Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putBoolean(BACKGROUND_CACHE, b);
        editor.apply();
    }

    public static boolean getBackgroundCacheEnabled(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(BACKGROUND_CACHE, true);
    }

    public static boolean getPushNotificationEnabled(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(PUSH_NOTIFICATIONS, true);
    }

    public static void setFontSize(Context context, int b) {
        Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putInt(FONT_SIZE, b);
        editor.apply();
    }

    public static int getFontSize(Context context) {
        return getDefaultSharedPreferences(context).getInt(FONT_SIZE, 12);
    }

    public static void setFirstRun(Context context, boolean b) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(FIRST_RUN, b);
        editor.commit();
    }

    public static boolean getFirstRun(Context context) {
        return getSharedPreferences(context).getBoolean(FIRST_RUN, true);
    }

    public static void setFCMSubmitted(Context context, boolean status) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(IS_FCM_SUBMITTED, status);
        editor.commit();
    }

    public static boolean isFCMSubmitted(Context context) {
        return getSharedPreferences(context).getBoolean(IS_FCM_SUBMITTED, false);
    }

    public static void setProfileFirstRun(Context context, boolean b) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(PROFILE_FIRST_RUN, b);
        editor.commit();
    }

    public static boolean getProfileFirstRun(Context context) {
        return getSharedPreferences(context).getBoolean(PROFILE_FIRST_RUN, true);
    }

    public static void setMygovAuthToken(Context context, String token, String name, String email, String refreshToken, boolean loggedIn) {
        Editor editor = getMygovAuthPreferences(context).edit();
        editor.putString("oauth_token", token);
        editor.putString(MYGOV_OAUTH_REFRESH_TOKEN, refreshToken);
        editor.putString(MYGOV_OAUTH_NAME, name);
        editor.putString(MYGOV_OAUTH_EMAIL, email);
        editor.putBoolean(PREF_MYGOV_LOGIN, loggedIn);
        editor.commit();
    }

    public static void setMygovOauthRefreshToken(Context context, String token) {
        Editor editor = getMygovAuthPreferences(context).edit();
        editor.putString(MYGOV_OAUTH_REFRESH_TOKEN, token);
        editor.commit();
    }

    public static void setMygovOauthAccessToken(Context context, String token) {
        Editor editor = getMygovAuthPreferences(context).edit();
        editor.putString("oauth_token", token);
        editor.commit();
    }

    public static void clearMygovCredentials(Context context) {
        Editor e = getMygovAuthPreferences(context).edit();
        e.remove("oauth_token");
        e.remove(MYGOV_OAUTH_REFRESH_TOKEN);
        e.remove(PREF_MYGOV_LOGIN);
        e.remove(MYGOV_OAUTH_NAME);
        e.remove(MYGOV_OAUTH_EMAIL);
        e.commit();
    }

    public static String getMygovOauthToken(Context context) {
        return getMygovAuthPreferences(context).getString("oauth_token", null);
    }

    public static String getMygovOauthRefreshToken(Context context) {
        return getMygovAuthPreferences(context).getString(MYGOV_OAUTH_REFRESH_TOKEN, null);
    }

    public static String getMygovOauthName(Context context) {
        return getMygovAuthPreferences(context).getString(MYGOV_OAUTH_NAME, null);
    }

    public static String getMygovOauthEmail(Context context) {
        return getMygovAuthPreferences(context).getString(MYGOV_OAUTH_EMAIL, null);
    }

    public static boolean getMygovLoggedIn(Context context) {
        return getMygovAuthPreferences(context).getBoolean(PREF_MYGOV_LOGIN, false);
    }

    public static String getServerUuid(Context context) {
        return getServerPreferences(context).getString(SERVER_UUID, null);
    }

    public static void setServerUuid(Context context, String uuid) {
        Editor editor = getServerPreferences(context).edit();
        editor.putString(SERVER_UUID, uuid);
        editor.commit();
    }

    public static String getFCMID(Context context) {
        return getServerPreferences(context).getString(FCM_ID, null);
    }

    public static void setFCMID(Context context, String uuid) {
        Editor editor = getServerPreferences(context).edit();
        editor.putString(FCM_ID, uuid);
        editor.commit();
    }


    public static String getPrefNotificationOnOf(Context context) {
        return getServerPreferences(context).getString(PREF_NOTIFICATION_ON_OF, "1");
    }

    public static void setPrefNotificationOnOf(Context context, String uuid) {
        Editor editor = getServerPreferences(context).edit();
        editor.putString(PREF_NOTIFICATION_ON_OF, uuid);
        editor.commit();
    }


    public static String getPrefMobileNumber(Context context) {
        return getServerPreferences(context).getString(PREF_MOBILE_NUMBER, "");
    }

    public static void setPrefPrefMobileNumber(Context context, String uuid) {
        Editor editor = getServerPreferences(context).edit();
        editor.putString(PREF_MOBILE_NUMBER, uuid);
        editor.commit();
    }

    public static String getIMEI(Context context) {
        return getServerPreferences(context).getString(DEVICE_IMEI, null);
    }

    public static void setIMEI(Context context, String uuid) {
        Editor editor = getServerPreferences(context).edit();
        editor.putString(DEVICE_IMEI, uuid);
        editor.commit();
    }

    public static String getServerDOB(Context context) {
        return getServerPreferences(context).getString(SERVER_USER_DOB, null);
    }

    public static void setServerDOB(Context context, String dob) {
        Editor editor = getServerPreferences(context).edit();
        editor.putString(SERVER_USER_DOB, dob);
        editor.commit();
    }

    public static String getServerDOA(Context context) {
        return getServerPreferences(context).getString(SERVER_USER_DOA, null);
    }

    public static void setServerDOA(Context context, String doa) {
        Editor editor = getServerPreferences(context).edit();
        editor.putString(SERVER_USER_DOA, doa);
        editor.commit();
    }

    public static void setGcmRegistered(Context context, boolean b) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(GCM_REGISTERED, b);
        editor.commit();
    }

    public static boolean getGcmRegistered(Context context) {
        return getSharedPreferences(context).getBoolean(GCM_REGISTERED, false);
    }

    public static String getGCMKey(Context context) {
        return getServerPreferences(context).getString(SERVER_GCM_key, null);
    }

    public static void setGCMKey(Context context, String key) {
        Editor editor = getServerPreferences(context).edit();
        editor.putString(SERVER_GCM_key, key);
        editor.commit();
    }

    public static long getLastLocationTime(Context context) {
        return getServerPreferences(context).getLong(LAST_LOCATION_TIME, 0);
    }

    public static void setLastLocationTime(Context context, long time) {
        Editor editor = getServerPreferences(context).edit();
        editor.putLong(LAST_LOCATION_TIME, time);
        editor.commit();
    }
}
