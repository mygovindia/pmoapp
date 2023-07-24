package com.sanskrit.pmo.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.sanskrit.pmo.Activities.LoginActivity;
import com.sanskrit.pmo.Activities.VoiceOutActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.Result;
import com.sanskrit.pmo.twitter.core.TwitterCore;
import com.sanskrit.pmo.twitter.core.TwitterException;
import com.sanskrit.pmo.twitter.core.TwitterSession;
import com.sanskrit.pmo.twitter.core.identity.TwitterLoginButton;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnProfileListener;


public class VoiceOutPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    Preference  facebook /*sms*/;
    SimpleFacebook simpleFacebook;
    ProgressDialog pDialog;

    //TwitterLoginButton loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.voiceout_preferences);

        PreferencesUtility.setOnSharedPreferenceChangeListener(getActivity(), this);
        simpleFacebook = SimpleFacebook.getInstance(getActivity());

       // twitter = (Preference) findPreference("preference_account_twitter");
        facebook = (Preference) findPreference("preference_account_facebook");
        //sms = (Preference) findPreference("preference_account_sms");

       /* sms.setEnabled(true);
        sms.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SMSSettingsDialog smsSettingsDialog = new SMSSettingsDialog();
                smsSettingsDialog.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "Dialog Fragment");
                return true;
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        getAccountStatus();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
    }


    private boolean isWhatsappInstalled() {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    private void getAccountStatus() {
        hideProgessBar1();
      //  new setTwitterAccount().execute("");

        try {
            if (simpleFacebook.isLogin()) {
                simpleFacebook.getProfile(onProfileListener);
                setLogoutfacebookListener();
            } else {
                facebook.setTitle(R.string.login_to_facebook);
                facebook.setSummary(R.string.account_not_authorized_facebook);
                setLoginFacebookListener();
                hideProgessBar2();
            }
        } catch (Exception e) {
            e.printStackTrace();
            facebook.setTitle(R.string.login_to_facebook);
            setLoginFacebookListener();
            hideProgessBar2();
        }

    }

    OnProfileListener onProfileListener = new OnProfileListener() {
        @Override
        public void onComplete(Profile profile) {
            facebook.setTitle(R.string.logout_from_facebook);
            facebook.setSummary(getString(R.string.logged_in_as) + profile.getName());
            facebook.setEnabled(true);
            hideProgessBar2();
        }

    };

    private void setLogoutfacebookListener() {
        facebook.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage(getActivity().getString(R.string.dialog_logging_out_facebook));
                    pDialog.setCancelable(false);
                    pDialog.dismiss();
                    simpleFacebook.logout(onLogoutListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

//    private void setTwitterLogoutListener() {
//        twitter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                PreferencesUtility.clearTwitterCredentials(getActivity());
//                TwitterCore.getInstance().logOut();
//                Snackbar.make(((VoiceOutActivity) getActivity()).coordinatorLayout, R.string.logged_out_from_twitter, Snackbar.LENGTH_SHORT).show();
//                getAccountStatus();
//                return true;
//            }
//        });
//    }

    OnLogoutListener onLogoutListener = new OnLogoutListener() {

        @Override
        public void onLogout() {
            if (getActivity() != null)
                Snackbar.make(((VoiceOutActivity) getActivity()).coordinatorLayout, R.string.logged_out_from_facebook, Snackbar.LENGTH_SHORT).show();
            pDialog.dismiss();
            getAccountStatus();
        }

    };

    private void setLoginFacebookListener() {
        facebook.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setAction("Facebook");
                startActivity(intent);
                return true;
            }
        });

    }

//    private void setLoginTwitterListener() {
//        twitter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                loginButton = new TwitterLoginButton(getActivity());
//
//                loginButton.setCallback(new Callback<TwitterSession>() {
//                    @Override
//                    public void success(Result<TwitterSession> result) {
//                        Toast.makeText(getActivity(), "Successfully logged in with Twitter", Toast.LENGTH_SHORT).show();
//                        new setTwitterAccount().execute("");
//                    }
//
//                    @Override
//                    public void failure(TwitterException exception) {
//                        Toast.makeText(getActivity(), "Error logging in with Twitter", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                loginButton.performClick();
//
//                return true;
//            }
//        });
//
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (loginButton != null)
//            loginButton.onActivityResult(requestCode, resultCode, data);
//    }

//    private class setTwitterAccount extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
//                if (session != null) {
//                    return session.getUserName();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            try {
//                if (result != null) {
//                    twitter.setSummary(getString(R.string.logged_in_as) + result);
//                    twitter.setTitle(getString(R.string.logout_from_twitter));
//                    setTwitterLogoutListener();
//                    twitter.setEnabled(true);
//                    hideProgessBar1();
//                } else {
//                    twitter.setTitle(getString(R.string.login_to_twitter));
//                    twitter.setSummary(getString(R.string.account_not_authorized_twitter));
//                    setLoginTwitterListener();
//                    hideProgessBar1();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//        }
//    }

    private void hideProgessBar1() {
        if (getActivity() != null)
            ((VoiceOutActivity) getActivity()).progressBar1.setVisibility(View.GONE);
    }

    private void hideProgessBar2() {
        if (getActivity() != null)
            ((VoiceOutActivity) getActivity()).progressBar2.setVisibility(View.GONE);
    }


}
