package com.sanskrit.pmo.Activities;

import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.sanskrit.pmo.Fragments.SignInFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.webview.CustomTabActivityHelper;


public class SignInActivity extends AppCompatActivity {
    private CustomTabActivityHelper mCustomTabActivityHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_setup_fragment_container);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignInFragment()).commit();
        setupCustomTabHelper();
    }

    protected void onStart() {
        super.onStart();
        this.mCustomTabActivityHelper.bindCustomTabsService(this);
    }

    protected void onStop() {
        super.onStop();
        this.mCustomTabActivityHelper.unbindCustomTabsService(this);
    }

    private void setupCustomTabHelper() {
        this.mCustomTabActivityHelper = new CustomTabActivityHelper();
        this.mCustomTabActivityHelper.setConnectionCallback(this.mConnectionCallback);
        this.mCustomTabActivityHelper.mayLaunchUrl(Uri.parse("https://secure.mygov.in/user/register"), null, null);
        this.mCustomTabActivityHelper.mayLaunchUrl(Uri.parse("https://auth.mygov.in/oauth2"), null, null);
    }


    private CustomTabActivityHelper.ConnectionCallback mConnectionCallback = new CustomTabActivityHelper.ConnectionCallback() {
        @Override
        public void onCustomTabsConnected() {

        }

        @Override
        public void onCustomTabsDisconnected() {

        }
    };

}
