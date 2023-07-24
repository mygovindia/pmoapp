package com.sanskrit.pmo.Services;

import android.content.Context;
import android.content.Intent;

import android.text.TextUtils;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sanskrit.pmo.Activities.MainActivity;
import com.sanskrit.pmo.Activities.NewsDetailActivity;
import com.sanskrit.pmo.App.Config;
import com.sanskrit.pmo.App.NotificationController;
import com.sanskrit.pmo.Models.NewsFeed;
import com.sanskrit.pmo.Models.NotificationInfo;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.NotificationUtils;
import com.sanskrit.pmo.network.mygov.models.NewsTag;
import com.sanskrit.pmo.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log.e(TAG, "From: " + remoteMessage.getFrom());
        //Log.e(TAG, "IS_PUSH_ENABLE: " + PreferencesUtility.getPushNotificationEnabled(this));

        if (!PreferencesUtility.getPushNotificationEnabled(this)) return;

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().toString());
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            //handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        String data = "{\n" +
                "\t\"ID\": \"4694661\",\n" +
                "\t\"title\": \"PM greets the nation on Dhanteras\",\n" +
                "\t\"content\": \"Hello! This is test message\",\n" +
                "\t\"excerpt\": \"\",\n" +
                "\t\"date\": \"\",\n" +
                "\t\"news_tweets\": \"\",\n" +
                "\t\"feature_image\": \"\",\n" +
                "\t\"tag_count\": 1,\n" +
                "\t\"tags\": [{\n" +
                "\t\t\"slug\": \"\",\n" +
                "\t\t\"name\": \"\",\n" +
                "\t\t\"term_taxonomy_id\": \"\"\n" +
                "\t}],\n" +
                "\t\"lang\": \"\",\n" +
                "\t\"notification_type\": \"latest_news\",\n" +
                "\t\"news_media\": \"\",\n" +
                "\t\"parent_id\": 0,\n" +
                "\t\"modified_date\": \"\"\n" +
                "}";
        //String content = data;
        if (remoteMessage.getData().size() > 0) {
            JSONObject object = new JSONObject(remoteMessage.getData());
            Log.e(TAG, "Data Payload: " + object);
            //Log.e(TAG, "Data Payload: " + content);
            try {
                //JSONObject json = new JSONObject(remoteMessage.getData().toString());
                //handleDataMessage(json);
                //NotificationInfo notificationInfo = handleDataPayloadMessage(remoteMessage.getData().toString());
                NotificationInfo notificationInfo = handleDataPayloadMessage(object.toString());
                //NotificationInfo notificationInfo = handleDataPayloadMessage(content);
                // NotificationController.init(notificationInfo);
                //NotificationController controller = new NotificationController(this, notificationInfo);
                //controller.init();
                showNotificationDataMessage(notificationInfo);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        Log.v(TAG, "handleNotification()=>:" + message);
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            //notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                //notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private NotificationInfo handleDataPayloadMessage(String response) {
        if (response == null) return null;
        Log.v(TAG, "NOTIFICATION_DATA: " + response);
        NotificationInfo details = new NotificationInfo();
        try {
            JSONObject object = new JSONObject(response);
            if (object.has("ID"))
                details.setId(object.getString("ID"));
            if (object.has("title"))
                details.setTitle(object.getString("title"));
            if (object.has("content"))
                details.setContent(object.getString("content"));
            if (object.has("excerpt"))
                details.setExcerpt(object.getString("excerpt"));
            if (object.has("date"))
                details.setDate(object.getString("date"));
            if (object.has("news_tweets"))
                details.setNewsTweets(object.getString("news_tweets"));
            if (object.has("feature_image"))
                details.setFeatureImage(object.getString("feature_image"));
            if (object.has("tag_count"))
                details.setTagCount(object.getInt("tag_count"));
            if (object.has("tags")) {
                List<NewsTag> tagsList = new ArrayList<>();
                JSONArray tagsArray = object.getJSONArray("tags");
                for (int i = 0; i < tagsArray.length(); i++) {
                    JSONObject jsonObject = tagsArray.getJSONObject(i);
                    NewsTag tags = new NewsTag();
                    tags.setmSLug(jsonObject.getString("slug"));
                    tags.setmName(jsonObject.getString("name"));
                    tags.setmTermId(jsonObject.getString("term_taxonomy_id"));
                    tagsList.add(tags);
                }
                details.setTagsList(tagsList);
            }
            if (object.has("lang"))
                details.setLang(object.getString("lang"));
            // if (object.has("news_tweets_ids"))
            //details.setNewsTweetsIDsList(object.getString("lang"));
            if (object.has("notification_type"))
                details.setNotificationType(object.getString("notification_type"));
            if (object.has("news_media"))
                details.setNewsMedia(object.getString("news_media"));
            if (object.has("parent_id"))
                details.setParentID(object.getInt("parent_id"));
            if (object.has("modified_date"))
                details.setModifiedDate(object.getString("modified_date"));
            if (object.has("imageInfo")) {
                List<NotificationInfo.GetImageInfo> imageInfoList = new ArrayList<>();
                JSONArray imageInfoArray = object.getJSONArray("imageInfo");
                for (int i = 0; i < imageInfoArray.length(); i++) {
                    JSONObject jsonObject = imageInfoArray.getJSONObject(i);
                    NotificationInfo.GetImageInfo imageInfo = new NotificationInfo.GetImageInfo();
                    imageInfo.setImageURL(jsonObject.getString("image_url"));
                    imageInfo.setImageCaption(jsonObject.getString("image_caption"));
                    imageInfoList.add(imageInfo);
                }
                details.setImageInfoList(imageInfoList);
            }
            if (object.has("link"))
                details.setLink(object.getString("link"));
            if (object.has("soundCloudLink"))
                details.setSoundCloudLink(object.getString("soundCloudLink"));
            if (object.has("createdAt"))
                details.setCreatedAt(object.getString("createdAt"));
            if (object.has("updatedAt"))
                details.setUpdatedAt(object.getString("updatedAt"));
            if (object.has("video_id"))
                details.setVideoID(object.getString("video_id"));
            if (object.has("objectId"))
                details.setObjectID(object.getString("objectId"));
            if (object.has("name"))
                details.setName(object.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }

    private void showNotificationDataMessage(NotificationInfo details) {
        try {
            if (details == null) return;

        /*Intent intent = new Intent(getApplicationContext(), NewsDetailActivity.class);
        intent.putExtra(Constants.NOTIFICATION_INFO, details);
        intent.putExtra("newsitem", Parcels.wrap(getNewsFeed(details)));
        intent.putExtra("shouldfetch", true);*/

            if (details.getNotificationType() != null && details.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_TYPE_GHOST))
                return;


            NotificationController controller = new NotificationController(this, details);
            Intent intent = controller.getResultIntent();

            //startActivity(intent);
            //navigateToNewsDetails(this,null,true);
            showNotificationMessage(getApplicationContext(), "PMIndia", details.getTitle() != null ? details.getTitle().trim() : "PMIndia", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), intent);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                startActivity(intent);
            }
        /*if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            if (details.getNotificationType() != null && details.getNotificationType().equalsIgnoreCase("latest_news")) {
                //navigateToNewsDetails(this,null,true);
                Intent intent = new Intent(getApplicationContext(), NewsDetailActivity.class);
                startActivity(intent);
                showNotificationMessage(getApplicationContext(), details.getTitle().trim(), "test 1", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()), intent);
            }

           *//* Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", details.getTitle() != null ? details.getTitle().trim() : "New Notification");
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);*//*

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // app is in background, show the notification in notification tray
            if (details.getNotificationType() != null && details.getNotificationType().equalsIgnoreCase("latest_news")) {
                Intent intent = new Intent(getApplicationContext(), NewsDetailActivity.class);
                startActivity(intent);
                //navigateToNewsDetails(this,null,true);
                showNotificationMessage(getApplicationContext(), details.getTitle().trim(), "test 2", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()), intent);

            }
            // check for image attachment
            *//*if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
            }*//*
        }*/
        }catch (Exception e){

        }
    }

    public static void navigateToNewsDetails(Context context, NewsFeed feed, boolean needsFetching) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        //intent.putExtra("newsitem", Parcels.wrap(feed));
        //intent.putExtra("shouldfetch", needsFetching);
        context.startActivity(intent);
    }
    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
        try {

            // Notify UI that registration has completed, so the progress indicator can be hidden.
            Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
            registrationComplete.putExtra("token", refreshedToken);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        }catch (Exception e){
            // Log.d("NEW_TOKEN",s);
        }
    }
    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        /*SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();*/
        try{
        PreferencesUtility.setFCMID(this, token);
        }catch (Exception e){

        }
    }
    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
      try{
        Log.v(TAG, "showNotificationMessage()=>Title: " + title + " <=> Message: " + message);
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
      }catch (Exception e){

      }
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        try{
        Log.v(TAG, "showNotificationMessageWithBigImage()=>Title: " + title + " <=> Message: " + message);
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
        }catch (Exception e){

        }
    }
}
