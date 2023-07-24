package com.sanskrit.pmo.player;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
//import android.support.v4.app.NotificationCompat;
//import android.support.v7.graphics.Palette;
import android.util.Log;

import com.facebook.login.widget.ProfilePictureView;
import com.sanskrit.pmo.Activities.MainActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.server.models.MkbAudio;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.DateUtil;

import static com.sanskrit.pmo.Utils.NotificationUtils.NOTIFICATION_CHANNEL_ID;

import androidx.core.app.NotificationCompat;
import androidx.palette.graphics.Palette;


public class MusicService extends Service implements OnPreparedListener, OnErrorListener, OnCompletionListener, OnAudioFocusChangeListener {
    public static final String CLOSE_ACTION = "com.sanskrit.pmo.player.close";
    public static final String CMDCLOSE = "close";
    public static final String CMDNAME = "command";
    public static final String CMDNEXT = "next";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPLAY = "play";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDSTOP = "stop";
    public static final String CMDTOGGLEPAUSE = "togglepause";
    public static final String NEXT_ACTION = "com.sanskrit.pmo.player.next";
    private static final int NOTIFY_MODE_BACKGROUND = 2;
    private static final int NOTIFY_MODE_FOREGROUND = 1;
    private static final int NOTIFY_MODE_NONE = 0;
    public static final String PAUSE_ACTION = "com.sanskrit.pmo.player.pause";
    public static final String PREVIOUS_ACTION = "com.sanskrit.pmo.player.previous";
    public static final String SERVICECMD = "com.sanskrit.pmo.player.musicservicecommand";
    public static final String STOP_ACTION = "com.sanskrit.pmo.player.stop";
    public static final String TOGGLEPAUSE_ACTION = "com.sanskrit.pmo.player.togglepause";
    private AudioManager audioManager;
    public boolean isPlayClicked = false;
    public boolean isPrepared = false;
    private MusicStateListener listener;
    private final BroadcastReceiver mIntentReceiver = new Receiver();
    private NotificationManager mNotificationManager;
    private long mNotificationPostTime = 0;
    private int mNotifyMode = 0;
    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer player;
    private MkbAudio track;
    String idChannel = "my_channel_01";

    class Receiver extends BroadcastReceiver {
        Receiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Log.d("service", "intentreciever");
            MusicService.this.handleCommandIntent(intent);
        }
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public IBinder onBind(Intent arg0) {
        return this.musicBind;
    }

    public boolean onUnbind(Intent intent) {
        try {
            this.player.stop();
            this.player.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onCreate() {
        super.onCreate();
        this.mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(TOGGLEPAUSE_ACTION);
        filter.addAction(PAUSE_ACTION);
        filter.addAction(STOP_ACTION);
        filter.addAction(NEXT_ACTION);
        filter.addAction(PREVIOUS_ACTION);
        registerReceiver(this.mIntentReceiver, filter);
        this.player = new MediaPlayer();
        initMusicPlayer();
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mIntentReceiver);
    }

    public void initMusicPlayer() {
        this.player.setWakeMode(getApplicationContext(), 1);
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.player.setAudioStreamType(3);
        this.player.setOnPreparedListener(this);
        this.player.setOnCompletionListener(this);
        this.player.setOnErrorListener(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            handleCommandIntent(intent);
        }
        return START_NOT_STICKY;
    }

    public void playSong() {
        this.isPlayClicked = true;
        if (this.listener != null) {
            Log.d("lol", "streaming");
            this.listener.onMusicStreaming();
        }
        if (this.player != null) {
            try {
                this.player.reset();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            this.player = new MediaPlayer();
            initMusicPlayer();
        }
        setBufferingUpdate();
        try {
            this.player.setDataSource(PreferencesUtility.getMkbCurrentTrackUrl(this));
        } catch (Exception e2) {
            Log.e("MUSIC SERVICE", "Error setting data source", e2);
        }
        try {
            this.player.prepareAsync();
        } catch (Exception e22) {
            Log.e("MUSIC SERVICE", "Error preparing", e22);
        }
    }

    public boolean isPlaying() {
        boolean z = false;
        try {
            if (this.player != null) {
                z = this.player.isPlaying();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return z;
    }

    public void play() {
        try {
            if (this.audioManager.requestAudioFocus(this, 3, 1) == 1) {
                this.isPlayClicked = true;
                if (this.player != null) {
                    this.player.start();
                    if (this.listener != null) {
                        this.listener.onStateChanged();
                        this.listener.onMusicStarted();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        try {
            this.isPlayClicked = false;
            if (this.player != null) {
                this.player.pause();
                if (this.listener != null) {
                    this.listener.onStateChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetPlayer() {
        try {
            this.isPlayClicked = false;
            if (this.player != null) {
                this.player.reset();
                if (this.listener != null) {
                    this.listener.onStateChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playOrPause() {
        try {
            if (this.player == null) {
                return;
            }
            if (isPlaying()) {
                pause();
            } else if (this.isPrepared) {
                play();
            } else if (this.isPlayClicked) {
                resetPlayer();
            } else {
                playSong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentTrack(MkbAudio track) {
        this.track = track;
    }

    public void stopPlayer() {
        try {
            this.isPlayClicked = false;
            if (this.player != null) {
                this.player.stop();
                if (this.listener != null) {
                    this.listener.onStateChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopAndRelease() {
        try {
            this.isPlayClicked = false;
            if (this.player != null) {
                this.player.stop();
                cancelNotification();
                if (this.listener != null) {
                    this.listener.onStateChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMusicStateLstener(MusicStateListener listener) {
        this.listener = listener;
    }

    public long getPosition() {
        try {
            if (this.player != null) {
                return (long) this.player.getCurrentPosition();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long getTotalDuration() {
        try {
            if (this.player != null) {
                return (long) this.player.getDuration();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setBufferingUpdate() {
        try {
            if (this.player != null) {
                this.player.setOnInfoListener(new OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {

                        switch (what) {
                            case 701:
                                if (MusicService.this.listener != null) {
                                    MusicService.this.listener.onMusicBufferingStarted();
                                    break;
                                }
                                break;
                            case 702:
                                if (MusicService.this.listener != null) {
                                    MusicService.this.listener.onMusicBufferingEnded();
                                    break;
                                }
                                break;
                        }
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTrackTitle() {
        if (this.track != null) {
            return this.track.mTitle;
        }
        return "";
    }


    public void onPrepared(MediaPlayer mp) {
        this.isPrepared = true;
        mp.start();
        updateNotification(false);
        if (this.listener != null) {
            this.listener.onMusicStarted();
            this.listener.onStateChanged();
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public void onCompletion(MediaPlayer mp) {
    }

    private void updateNotification(boolean recentlyplayes) {
        int newNotifyMode;
        if (isPlaying()) {
            newNotifyMode = 1;
        } else if (recentlyplayes) {
            newNotifyMode = 2;
        } else {
            newNotifyMode = 0;
        }
        int notificationId = hashCode();
        if (this.mNotifyMode != newNotifyMode) {
            if (this.mNotifyMode == 1) {
                stopForeground(newNotifyMode == 0);
            } else if (newNotifyMode == 0) {
                this.mNotificationManager.cancel(notificationId);
                this.mNotificationPostTime = 0;
            }
        }
        if (newNotifyMode == 1) {
            startForeground(55, buildNotification());
        } else if (newNotifyMode == 2) {
            this.mNotificationManager.notify(notificationId, buildNotification());
        }
        this.mNotifyMode = newNotifyMode;
    }

    private void cancelNotification() {
        stopForeground(true);
        this.mNotificationManager.cancel(hashCode());
        this.mNotificationPostTime = 0;
        this.mNotifyMode = 0;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private Notification buildNotification() {
        int playButtonResId = isPlaying() ? R.mipmap.ic_pause_white_36dp : R.mipmap.ic_play_white_36dp;
        int playButtonTitleResId = isPlaying() ? R.string.accessibility_pause : R.string.accessibility_play;
        Intent nowPlayingIntent = new Intent(this, MainActivity.class);
        nowPlayingIntent.putExtra(Constants.POSITION, 2);
        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (this.mNotificationPostTime == 0) {
            this.mNotificationPostTime = System.currentTimeMillis();
        }
        Bitmap artwork = BitmapFactory.decodeResource(getResources(), R.drawable.mannkibaat);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "MyApp events", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            return new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_stat_notif)
                    .setLargeIcon(artwork)
                    .setContentIntent(clickIntent)
                    .setContentTitle(PreferencesUtility.getMkbCurrentTrack(this))
                    .setContentText(DateUtil.dateToString(DateUtil.stringToDate(PreferencesUtility.getMkbCurrentTrackdate(this))))
                    .setWhen(mNotificationPostTime)
                    .addAction(playButtonResId, getString(playButtonTitleResId),
                            retrievePlaybackAction(TOGGLEPAUSE_ACTION))
                    .addAction(R.mipmap.ic_close_white_48dp,
                            getString(R.string.accessibility_prev),
                            retrievePlaybackAction(CLOSE_ACTION)).setChannelId(NOTIFICATION_CHANNEL_ID)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1))
                    .setShowWhen(false)
                    .setColor(Palette.from(artwork).generate().getDarkVibrantColor(Color.parseColor("#403f4d")))
                    .build();
        } else {


            return new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_stat_notif)
                    .setLargeIcon(artwork)
                    .setContentIntent(clickIntent)
                    .setContentTitle(PreferencesUtility.getMkbCurrentTrack(this))
                    .setContentText(DateUtil.dateToString(DateUtil.stringToDate(PreferencesUtility.getMkbCurrentTrackdate(this))))
                    .setWhen(mNotificationPostTime)
                    .addAction(playButtonResId, getString(playButtonTitleResId),
                            retrievePlaybackAction(TOGGLEPAUSE_ACTION))
                    .addAction(R.mipmap.ic_close_white_48dp,
                            getString(R.string.accessibility_prev),
                            retrievePlaybackAction(CLOSE_ACTION))
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1))
                    .setShowWhen(false)
                    .setColor(Palette.from(artwork).generate().getDarkVibrantColor(Color.parseColor("#403f4d")))
                    .build();


        }
    }

    private PendingIntent retrievePlaybackAction(String action) {
        Log.d("service", "retrieveplayback");
        ComponentName serviceName = new ComponentName(this, MusicService.class);
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);
        return PendingIntent.getService(this, 0, intent, 0);
    }

    private void handleCommandIntent(Intent intent) {
        Log.d("service", "handelcommand");
        String action = intent.getAction();
        String command = SERVICECMD.equals(action) ? intent.getStringExtra(CMDNAME) : null;
        if (CMDCLOSE.equals(command) || CLOSE_ACTION.equals(action)) {
            try {
                this.player.stop();
                this.player.reset();
                this.player.release();
                this.listener.onStateChanged();
                cancelNotification();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!CMDPREVIOUS.equals(command) && !PREVIOUS_ACTION.equals(action)) {
            if (CMDTOGGLEPAUSE.equals(command) || TOGGLEPAUSE_ACTION.equals(action)) {
                if (isPlaying()) {
                    pause();
                } else {
                    play();
                }
                updateNotification(true);
            } else if (CMDPAUSE.equals(command) || PAUSE_ACTION.equals(action)) {
                pause();
            } else if (CMDPLAY.equals(command)) {
                play();
            } else if (!CMDSTOP.equals(command) && STOP_ACTION.equals(action)) {
            }
        }
    }

    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case ProfilePictureView.NORMAL /*-3*/:
                try {
                    if (this.player.isPlaying()) {
                        this.player.setVolume(0.1f, 0.1f);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            case -2:
                try {
                    if (this.player.isPlaying()) {
                        pause();
                        return;
                    }
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            case -1:
                try {
                    if (this.player.isPlaying()) {
                        stopPlayer();
                    }
                    this.player.release();
                    this.player = null;
                    return;
                } catch (Exception e22) {
                    e22.printStackTrace();
                    return;
                }
            case 1:
                try {
                    if (this.player == null) {
                        initMusicPlayer();
                    } else if (!this.player.isPlaying()) {
                        play();
                    }
                    this.player.setVolume(1.0f, 1.0f);
                    return;
                } catch (Exception e222) {
                    e222.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }
}
