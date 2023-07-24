package com.sanskrit.pmo.Utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.sanskrit.pmo.App.Config;
import com.sanskrit.pmo.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;


public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();
    public static final String NOTIFICATION_CHANNEL_ID = "my_channel_01";

    private Context mContext;

    private int notificationID;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    private final static AtomicInteger c = new AtomicInteger(0);

    private void getID() {
        notificationID = c.incrementAndGet();
    }

    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.US).format(now));
        return id;
    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
        showNotificationMessage(title, message, timeStamp, intent, null);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotifChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                "MyApp events", NotificationManager.IMPORTANCE_LOW);
        // Configure the notification channel
        channel.setDescription("MyApp event controls");
        channel.setShowBadge(false);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager manager = context.getSystemService(NotificationManager.class);

        manager.createNotificationChannel(channel);
        Log.d(TAG, "createNotifChannel: created=" + NOTIFICATION_CHANNEL_ID);
    }

    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;

        getID();

        // notification icon
        final int icon = R.mipmap.ic_launcher;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_ONE_SHOT
                );

        /*PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT);*/

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/notification");

        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                } else {
                    showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                }
            }
        } else {
            showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);

        }
    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                //.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setSound(getNotificationSoundURI() != null ? getNotificationSoundURI() : Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(new long[]{100, 250, 100, 250, 100, 250})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .build();

               /* .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(new long[]{500, 500})
                .setLights(Color.RED, 3000, 3000)*/

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
                createNotifChannel(mContext);
            }
        }
        notificationManager.notify(notificationID, notification);

        //playNotificationSound();
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        // White icon must be transparent
        return useWhiteIcon ? R.drawable.ic_national_emblem : R.mipmap.ic_launcher;
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    private void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Uri getNotificationSoundURI() {
        try {
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*private static void generateNotification(Context context, String message,
                                             String keys, String msgId, String branchId) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationCompat.Builder nBuilder;
        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        nBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Smart Share - " + keys)
                .setLights(Color.BLUE, 500, 500).setContentText(message)
                .setAutoCancel(true).setTicker("Notification from smartshare")
                .setVibrate(new long[] { 100, 250, 100, 250, 100, 250 })
                .setSound(alarmSound);
        String consumerid = null;
        Integer position = null;
        Intent resultIntent = null;
        if (consumerid != null) {
            if (msgId != null && !msgId.equalsIgnoreCase("")) {
                if (key != null && key.equalsIgnoreCase("Yo! Matter")) {
                    ViewYoDataBase db_yo = new ViewYoDataBase(context);
                    position = db_yo.getPosition(msgId);
                    if (position != null) {
                        resultIntent = new Intent(context,
                                YoDetailActivity.class);
                        resultIntent.putExtra("id", Integer.parseInt(msgId));
                        resultIntent.putExtra("position", position);
                        resultIntent.putExtra("notRefresh", "notRefresh");
                    } else {
                        resultIntent = new Intent(context,
                                FragmentChangeActivity.class);
                        resultIntent.putExtra(key, key);
                    }
                } else if (key != null && key.equalsIgnoreCase("Message")) {
                    resultIntent = new Intent(context,
                            FragmentChangeActivity.class);
                    resultIntent.putExtra(key, key);
                }.
.
.
.
.
.
            } else {
                resultIntent = new Intent(context, FragmentChangeActivity.class);
                resultIntent.putExtra(key, key);
            }
        } else {
            resultIntent = new Intent(context, MainLoginSignUpActivity.class);
        }
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                notify_no, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (notify_no < 9) {
            notify_no = notify_no + 1;
        } else {
            notify_no = 0;
        }
        nBuilder.setContentIntent(resultPendingIntent);
        NotificationManager nNotifyMgr = (NotificationManager) context
                .getSystemService(context.NOTIFICATION_SERVICE);
        nNotifyMgr.notify(notify_no + 2, nBuilder.build());
    }*/
}
