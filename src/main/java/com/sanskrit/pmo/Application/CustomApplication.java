package com.sanskrit.pmo.Application;

import android.app.Application;

import com.anupcowkur.reservoir.Reservoir;

//import com.crashlytics.android.Crashlytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
//import com.google.firebase.crashlytics.internal.common.CrashlyticsCore;
import com.sanskrit.pmo.permissions.Nammu;
import com.sanskrit.pmo.twitter.core.TwitterAuthConfig;
import com.sanskrit.pmo.twitter.core.TwitterCore;
import com.sanskrit.pmo.twitter.tweetui.TweetUi;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

import io.fabric.sdk.android.Fabric;


/**
 * Created by manoj on 22/12/17.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Nammu.init(this);

        TwitterAuthConfig authConfig = new TwitterAuthConfig("vtB0MYXml7XilyzlMPnNsJMJJ", "Nu90KuCFIdcTiZeSoR347UMvOOFARyOft0DciIP9Mr2lJVPvUk");


              //  FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
       // Fabric.with(this, new Crashlytics(), new TwitterCore(authConfig), new TweetUi());


        Permission[] permissions = new Permission[]{
                Permission.PUBLISH_ACTION
        };

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId("141998196152009")
                .setPermissions(permissions)
                .setNamespace("sanskritpmo")
                .setAskForAllPermissionsAtOnce(false)
                .build();

        SimpleFacebook.setConfiguration(configuration);
        try {
            Reservoir.init(this, 2048 * 1024 * 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
