package com.sanskrit.pmo.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sanskrit.pmo.Activities.NewsDetailActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.Result;
import com.sanskrit.pmo.twitter.core.TwitterException;
import com.sanskrit.pmo.twitter.core.TwitterSession;
import com.sanskrit.pmo.twitter.core.identity.TwitterLoginButton;


public class TwitterLoginFragment extends BottomSheetDialogFragment {
    TwitterLoginButton loginButton;
    private BottomSheetBehavior mBehavior;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_login_twitter, null);
        dialog.setContentView(view);
        this.mBehavior = BottomSheetBehavior.from((View) view.getParent());
        this.loginButton = (TwitterLoginButton) view.findViewById(R.id.login_button);

        this.loginButton.setCallback(new Callback<TwitterSession>() {
            public void success(Result<TwitterSession> result) {
                Toast.makeText(TwitterLoginFragment.this.getActivity(), "Successfully logged in with Twitter", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                try {
                    Fragment parent = TwitterLoginFragment.this.getParentFragment();
                    if (parent != null && (parent instanceof TweetsFragment)) {
                        ((TweetsFragment) parent).transactFragment(new com.sanskrit.pmo.fragments.AuthenticatedTweetsFragment());
                    }
                    Activity activity = TwitterLoginFragment.this.getActivity();
                    if (activity != null && (activity instanceof NewsDetailActivity)) {
                        ((NewsDetailActivity) activity).loadAuthenticatedTweets();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void failure(TwitterException exception) {
                Toast.makeText(TwitterLoginFragment.this.getActivity(), "Error logging in with Twitter", Toast.LENGTH_SHORT).show();
            }
        });
        return dialog;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.loginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void onStart() {
        super.onStart();
        this.mBehavior.setState(3);
    }
}
