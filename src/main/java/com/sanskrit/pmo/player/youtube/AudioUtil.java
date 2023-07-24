package com.sanskrit.pmo.player.youtube;

import android.content.Context;
import android.media.AudioManager;

public class AudioUtil {
    private static AudioManager audioManager;
    private static final Object mSingletonLock = new Object();

    private static AudioManager getInstance(Context context) {
        AudioManager audioManager = null;
        synchronized (mSingletonLock) {
            if (audioManager != null) {
                audioManager = audioManager;
            } else {
                if (context != null) {
                    audioManager = (AudioManager) context.getSystemService("audio");
                }
                audioManager = audioManager;
            }
        }
        return audioManager;
    }

    public static void adjustMusicVolume(Context context, boolean up, boolean showInterface) {
        int i = 1;
        int direction = up ? 1 : -1;
        if (!showInterface) {
            i = 0;
        }
        getInstance(context).adjustStreamVolume(3, direction, i | 8);
    }

    public static void playKeyClickSound(Context context, int volume) {
        if (volume != 0) {
            getInstance(context).playSoundEffect(0, ((float) volume) / 100.0f);
        }
    }
}
