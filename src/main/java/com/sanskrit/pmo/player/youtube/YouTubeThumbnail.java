package com.sanskrit.pmo.player.youtube;



import androidx.annotation.NonNull;

public class YouTubeThumbnail {
    public static String getUrlFromVideoId(@NonNull String videoId, @NonNull Quality quality) {
        switch (quality) {
            case FIRST:
                return "http://img.youtube.com/vi/" + videoId + "/0.jpg";
            case SECOND:
                return "http://img.youtube.com/vi/" + videoId + "/1.jpg";
            case THIRD:
                return "http://img.youtube.com/vi/" + videoId + "/2.jpg";
            case FOURTH:
                return "http://img.youtube.com/vi/" + videoId + "/3.jpg";
            case MAXIMUM:
                return "http://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg";
            case STANDARD_DEFINITION:
                return "http://img.youtube.com/vi/" + videoId + "/sddefault.jpg";
            case MEDIUM:
                return "http://img.youtube.com/vi/" + videoId + "/mqdefault.jpg";
            case HIGH:
                return "http://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
            default:
                return "http://img.youtube.com/vi/" + videoId + "/default.jpg";
        }
    }
}
