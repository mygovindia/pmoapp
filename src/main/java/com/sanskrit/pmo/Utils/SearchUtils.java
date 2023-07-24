package com.sanskrit.pmo.Utils;

import android.content.Context;
import android.content.Intent;

import com.sanskrit.pmo.Activities.AboutPMActivity;
import com.sanskrit.pmo.Activities.FormerPMActivity;
import com.sanskrit.pmo.Activities.FunctionalChartActivity;
import com.sanskrit.pmo.Activities.GalleryActivity;
import com.sanskrit.pmo.Activities.MKBActivity;
import com.sanskrit.pmo.Activities.MainActivity;
import com.sanskrit.pmo.Activities.QuotesActivity;
import com.sanskrit.pmo.Activities.SettingsActivity;
import com.sanskrit.pmo.Activities.VoiceOutActivity;
import com.sanskrit.pmo.utils.Constants;

import java.util.ArrayList;

public class SearchUtils {
    public static ArrayList<String> getApplicationContentArray() {
        ArrayList<String> applicationResults = new ArrayList();
        applicationResults.add("Mann ki baat");
        applicationResults.add("Quotes");
        applicationResults.add("About Prime Minister");
        applicationResults.add("Latest News");
        applicationResults.add("Image Gallery");
        applicationResults.add("Former Prime Ministers");
        applicationResults.add("Functional Chart");
        applicationResults.add("Settings");
        applicationResults.add("Social Feed");
        applicationResults.add("Notifications");
       // applicationResults.add("Voice Out");
        applicationResults.add("Daydream");
        return applicationResults;
    }

    public static Intent getIntentForAppResults(Context context, int id) {
        Intent intent;
        switch (id) {
            case 0:
                intent = new Intent(context, MKBActivity.class);
                intent.setAction("Audio");
                return intent;
            case 1:
                return new Intent(context, QuotesActivity.class);
            case 2:
                return new Intent(context, AboutPMActivity.class);
            case 3:
                return new Intent(context, MainActivity.class);
            case 4:
                return new Intent(context, GalleryActivity.class);
            case 5:
                return new Intent(context, FormerPMActivity.class);
            case 6:
                return new Intent(context, FunctionalChartActivity.class);
            case 7:
                return new Intent(context, SettingsActivity.class);
            case 8:
                intent = new Intent(context, MainActivity.class);
                intent.putExtra(Constants.POSITION, 1);
                return intent;
            case 9:
                intent = new Intent(context, MainActivity.class);
                intent.putExtra(Constants.POSITION, 3);
                return intent;
//            case 10:
//                return new Intent(context, VoiceOutActivity.class);
            case 10:
                return new Intent("android.settings.DISPLAY_SETTINGS");
            default:
                return new Intent(context, MainActivity.class);
        }
    }
}
