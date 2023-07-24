package com.sanskrit.pmo.utils;

import java.util.Locale;

final class MediaTimeUtils {
    private static final String TIME_FORMAT_LONG = "%1$d:%2$02d:%3$02d";
    private static final String TIME_FORMAT_SHORT = "%1$d:%2$02d";

    private MediaTimeUtils() {
    }

    static String getPlaybackTime(long timeMillis) {
        int timeSeconds = (int) (timeMillis / 1000);
        int seconds = timeSeconds % 60;
        int minutes = (timeSeconds / 60) % 60;
        int hours = ((timeSeconds / 60) / 60) % 60;
        if (timeSeconds / 3600 > 0) {
            return String.format(Locale.getDefault(), TIME_FORMAT_LONG, new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)});
        }
        return String.format(Locale.getDefault(), TIME_FORMAT_SHORT, new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)});
    }
}
