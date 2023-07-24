package com.sanskrit.pmo.player.youtube;



import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeUrlParser {
    static final String reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";

    public static String getVideoId(@NonNull String videoUrl) {
        Matcher matcher = Pattern.compile(reg, 2).matcher(videoUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getVideoUrl(@NonNull String videoId) {
        return "http://youtu.be/" + videoId;
    }

    public static String getVideoSecuredUrl(@NonNull String videoId) {
        return "https://youtu.be/" + videoId;
    }
}
