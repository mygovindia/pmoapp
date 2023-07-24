package com.sanskrit.pmo.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;

import com.sanskrit.pmo.Activities.MainActivity;
import com.sanskrit.pmo.Activities.NativeLoginActivity;
import com.sanskrit.pmo.Activities.NativeSignupActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.webview.CustomTabActivityHelper;
import com.sanskrit.pmo.webview.WebviewFallback;

public class SignInFragment extends Fragment {
    ImageButton next;
    Button skip;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.appsetup_signin, container, false);
        ((Button) rootView.findViewById(R.id.loginMygov)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInFragment.this.getActivity(), NativeLoginActivity.class);
                intent.setAction("MyGov");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SignInFragment.this.startActivity(intent);
            }
        });
        ((Button) rootView.findViewById(R.id.createMyGovAccount)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // SignInFragment.this.launchCustomTab("https://secure.mygov.in/user/register");
                Intent intent = new Intent(SignInFragment.this.getActivity(), NativeSignupActivity.class);
                intent.setAction("MyGov");
                SignInFragment.this.startActivity(intent);

            }
        });
        this.next = (ImageButton) rootView.findViewById(R.id.next);
        this.skip = (Button) rootView.findViewById(R.id.skip);


        this.skip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtility.setFirstRun(SignInFragment.this.getActivity(), false);
                PreferencesUtility.setProfileFirstRun(SignInFragment.this.getActivity(), false);
                Intent intent = new Intent(SignInFragment.this.getActivity().getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                SignInFragment.this.startActivity(intent);

            }
        });
        return rootView;
    }

    private void launchCustomTab(String url) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        intentBuilder.setShowTitle(true);
        CustomTabActivityHelper.openCustomTab(getActivity(), intentBuilder.build(), Uri.parse(url), new WebviewFallback(), false);
    }
}
