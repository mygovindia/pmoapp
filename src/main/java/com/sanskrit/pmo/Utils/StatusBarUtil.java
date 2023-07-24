package com.sanskrit.pmo.Utils;

import android.app.Activity;
import android.os.Build.VERSION;

public class StatusBarUtil {
    public static void hide(Activity activity) {
        if (VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(1024, 1024);
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(4);
        }
    }
}
