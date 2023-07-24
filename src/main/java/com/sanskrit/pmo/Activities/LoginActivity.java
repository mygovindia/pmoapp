package com.sanskrit.pmo.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.facebook.internal.NativeProtocol;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.server.SanskritCallbackClient;
import com.sanskrit.pmo.network.server.SanskritClient;
import com.sanskrit.pmo.network.server.callbacks.RequestTokenListener;
import com.sanskrit.pmo.network.server.callbacks.UserLoginListener;
import com.sanskrit.pmo.network.server.callbacks.UserProfileListener;
import com.sanskrit.pmo.network.server.callbacks.UserSignupListener;
import com.sanskrit.pmo.network.server.models.RequestToken;
import com.sanskrit.pmo.network.server.models.UserLogin;
import com.sanskrit.pmo.network.server.models.UserProfileMygov;
import com.sanskrit.pmo.network.server.models.UserSignup;
import com.sanskrit.pmo.utils.Utils;
import com.sanskrit.pmo.webview.CustomTabActivityHelper;
import com.sanskrit.pmo.webview.WebviewFallback;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    public static final String REG_ID = "regId";
    private static final String TAG = LoginActivity.class.getSimpleName();
    private String MYGOV_CLIENT_ID = "pmoappteamsanskrit";
    private String MYGOV_SCOPE = "user_profile";
    private String REDIRECT_URI = "sanskritpmo://oauthflowredirect";
    private String REDIRECT_URI_MYGOV = "http://164.100.94.136/teamsanskrit/callback/oauthflowredirect";
    private String STATE = "sanskrit_pmo_random_state";
    private String action;
    public final String BASE_URL_REQUEST_TOKEN_MYGOV = ("https://auth.mygov.in/oauth2/authorize?response_type=code&client_id=" + this.MYGOV_CLIENT_ID + "&redirect_uri=" + this.REDIRECT_URI_MYGOV + "&scope=" + this.MYGOV_SCOPE + "&state=" + this.STATE);
    Context applicationContext;
    private boolean customTabLaunched = false;
    private boolean dueToNewIntent = false;
    GoogleCloudMessaging gcmObj;
    TextView loginStatus;
    private CustomTabActivityHelper mCustomTabActivityHelper;
    OnLoginListener onLoginListener;
    private boolean onResumeCalled = false;
    ProgressDialog pDialog;

    ProgressBar progressBar;
    String regId = null;
    SimpleFacebook simpleFacebook;
    WebView webView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login);
        this.action = getIntent().getAction();
        this.applicationContext = this;
        this.loginStatus = (TextView) findViewById(R.id.login_status);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.webView = (WebView) findViewById(R.id.webview);
        if (!this.action.contains("Intent")) {
            setLoginStatusToHeader(getString(R.string.connecting_with) + this.action + "...");
        }
        if (this.action.equals("Facebook") && Utils.is15()) {

            this.simpleFacebook = SimpleFacebook.getInstance(this);
            setFacebookLogin();
        }

        getRequestToken();
    }


    protected void onResume() {
        super.onResume();
        if (Utils.is15()) {
            this.simpleFacebook = SimpleFacebook.getInstance(this);
        }
        if (this.onResumeCalled && this.customTabLaunched && !this.dueToNewIntent) {
            // Toast.makeText(this, R.string.some_error_occurred, Toast.LENGTH_SHORT).show();
            finish();
        }
        this.onResumeCalled = true;
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        this.dueToNewIntent = true;
        if (uri == null || !uri.toString().startsWith(this.REDIRECT_URI)) {
            Toast.makeText(this, R.string.some_error_occurred, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String str = this.action;
        if (str.equals("MyGov")) {
            String afterDecode;
            try {
                afterDecode = URLDecoder.decode(uri.getQueryParameter("response"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                afterDecode = "";
            }
            try {
                JSONObject object = new JSONObject(afterDecode);
                if (object.has("error")) {
                    Toast.makeText(this, R.string.error_logging_in, Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", object.getString("error") + " " + object.getString(NativeProtocol.BRIDGE_ARG_ERROR_DESCRIPTION));
                    finish();
                    return;
                }
                setLoginStatusToHeader(getString(R.string.fetching_profile));
                final String token = object.getString("access_token");
                final String refreshToken = object.getString("refresh_token");
                SanskritCallbackClient.getInstance(this).getUserProfile(token, new UserProfileListener() {
                    public void success(UserProfileMygov userProfile) {
                        if (userProfile != null) {
                            PreferencesUtility.setMygovAuthToken(LoginActivity.this, token, userProfile.mFullName, userProfile.mEmail, refreshToken, true);
                            LoginActivity.this.attemptServerLogin(userProfile.mEmail, userProfile.mFullName);
                            return;
                        }
                        Log.d("lol", "userprofile is null!!!");
                        Toast.makeText(LoginActivity.this, R.string.error_fetching_profile, Toast.LENGTH_SHORT).show();
                        LoginActivity.this.finish();
                    }

                    public void failure() {
                        Toast.makeText(LoginActivity.this, R.string.error_fetching_profile, Toast.LENGTH_SHORT).show();
                        LoginActivity.this.finish();
                    }
                });
                return;
            } catch (JSONException e2) {
                e2.printStackTrace();
                Toast.makeText(this, R.string.error_logging_in, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

    }

    protected void onStart() {
        super.onStart();
        this.mCustomTabActivityHelper.bindCustomTabsService(this);
    }

    protected void onStop() {
        super.onStop();
        this.mCustomTabActivityHelper.unbindCustomTabsService(this);
    }

    private void getRequestToken() {
        setupCustomTabHelper();
        String str = this.action;

        if (str.equals("MyGov")) {
            launchCustomTab(getBaseApiUrl());
        } else if (str.equals("Facebook")) {
            this.simpleFacebook.login(this.onLoginListener);
        }

    }

    private void launchCustomTab(String uri) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        intentBuilder.setShowTitle(true);
        CustomTabActivityHelper.openCustomTab(this, intentBuilder.build(), Uri.parse(uri), new WebviewFallback(), false);
        this.customTabLaunched = true;
    }

    private void setupCustomTabHelper() {
        this.mCustomTabActivityHelper = new CustomTabActivityHelper();
        this.mCustomTabActivityHelper.setConnectionCallback(this.mConnectionCallback);
        this.mCustomTabActivityHelper.mayLaunchUrl(Uri.parse(getBaseApiUrl()), null, null);
    }

    private void setLoginStatusToHeader(String status) {
        this.loginStatus.setText(status);
    }

    private void stopProgressBar() {
        this.progressBar.setVisibility(View.GONE);
    }

    private void startProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    private void attemptServerLogin(final String email, final String name) {
        SanskritClient.getInstance(this).getRequestToken(Utils.getPermaToken(this), new RequestTokenListener() {
            public void success(RequestToken token) {
                if (token.mErrorResponse == null) {
                    LoginActivity.this.checkUserExistsOnServer(token.mToken, email, name);
                } else {
                    Log.d("lol11", "errorrrrr");
                }
            }

            public void failure() {
            }
        });
    }

    private void checkUserExistsOnServer(String token, final String email, final String name) {
        SanskritClient.getInstance(this).loginUser(token, email, "", new UserLoginListener() {
            public void success(UserLogin userLogin) {
                if (userLogin == null) {
                    Toast.makeText(LoginActivity.this, R.string.some_error_occurred, Toast.LENGTH_SHORT).show();
                    LoginActivity.this.finish();
                } else if (userLogin.mErrorResponse == null) {
                    LoginActivity.this.registerToGcmInBackground(email, name, false, userLogin.mUUID, userLogin);
                } else if (userLogin.mErrorCode == 10) {
                    Log.d("server", "error attepting to check on database");
                } else if (userLogin.mErrorCode == 100) {
                    LoginActivity.this.registerToGcmInBackground(email, name, true, LoginActivity.this.getUUid(), null);
                }
            }

            public void failure() {
            }
        });
    }

    public void onWebviewPageFinished() {
        stopProgressBar();
    }

    private String getBaseApiUrl() {
        String str = this.action;


        if (str.equals("MyGov")) {
            return this.BASE_URL_REQUEST_TOKEN_MYGOV;
        } else {
            return "";
        }
    }

    private void setFacebookLogin() {
        this.onLoginListener = new OnLoginListener() {
            public void onLogin(String accessToken, List<Permission> list, List<Permission> list2) {
                Toast.makeText(LoginActivity.this, R.string.log_in_success, Toast.LENGTH_SHORT).show();
                LoginActivity.this.finish();
            }

            public void onCancel() {
                // Toast.makeText(LoginActivity.this, R.string.some_error_occurred, Toast.LENGTH_SHORT).show();
                LoginActivity.this.finish();
            }

            public void onFail(String reason) {
                //if(reason.contains("Cancel"))
                //   Toast.makeText(LoginActivity.this, reason+"", Toast.LENGTH_SHORT).show();
                LoginActivity.this.finish();
            }

            public void onException(Throwable throwable) {
                Toast.makeText(LoginActivity.this, R.string.some_error_occurred, Toast.LENGTH_SHORT).show();
                LoginActivity.this.finish();
            }
        };


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.simpleFacebook.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void registerToGcmInBackground(String emailID, String name, boolean signUpUser, String uuid, UserLogin userLogin) {
        final String str = emailID;
        final String str2 = name;
        final boolean z = signUpUser;
        final String str3 = uuid;
        final UserLogin userLogin2 = userLogin;
        new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    {
                        if (LoginActivity.this.gcmObj == null) {
                            LoginActivity.this.gcmObj = GoogleCloudMessaging.getInstance(LoginActivity.this);
                        }
                        LoginActivity.this.regId = LoginActivity.this.gcmObj.register("339180625670");
                        return "Registration ID :" + LoginActivity.this.regId;
                    }

                } catch (IOException ex) {
                    return "Error :" + ex.getMessage();
                }
            }

            protected void onPostExecute(String msg) {
                if (LoginActivity.this.regId != null) {
                    LoginActivity.this.getAccessTokenAndSignup(LoginActivity.this.regId, str, str2, z, str3, userLogin2);
                    Log.d("server", "registered with gcm");
                    return;
                }
                LoginActivity.this.getAccessTokenAndSignup("", str, str2, z, str3, userLogin2);
                Log.d("server", "registration to gcm failed");
            }
        }.execute(new Void[]{null, null, null});
    }

    private void getAccessTokenAndSignup(String gcm, String emailID, String name, boolean signUpUser, String uuid, UserLogin userLogin) {
        final String str = gcm;
        final String str2 = emailID;
        final String str3 = name;
        final boolean z = signUpUser;
        final String str4 = uuid;
        final UserLogin userLogin2 = userLogin;
        SanskritClient.getInstance(this).getRequestToken(Utils.getPermaToken(this), new RequestTokenListener() {
            public void success(RequestToken token) {
                if (token.mErrorResponse == null) {
                    LoginActivity.this.signUp(token.mToken, str, str2, str3, z, str4, userLogin2);
                } else {
                    Log.d("lol11", "errorrrrr");
                }
            }

            public void failure() {
            }
        });
    }

    private void signUp(String token, String gcm, String emailID, String name, boolean signUpUser, String uuid, UserLogin userLogin) {
        String mDOB;
        String mDOA;
        if (userLogin == null) {
            mDOB = "";
            mDOA = "";
        } else {
            mDOB = userLogin.mDOB;
            mDOA = userLogin.mDOA;
        }
        final String str = gcm;
        final boolean z = signUpUser;
        SanskritClient.getInstance(this).signUpUser(emailID, "", mDOA, mDOB, name, 0, signUpUser, uuid, gcm, 1, token, new UserSignupListener() {
            public void success(UserSignup userSignup) {
                Log.v(TAG, "signUP()=>success()");
                if (userSignup.mErrorResponse == null) {
                    PreferencesUtility.setFirstRun(LoginActivity.this, false);
                    PreferencesUtility.setServerUuid(LoginActivity.this, userSignup.uuid);
                    PreferencesUtility.setServerDOA(LoginActivity.this, userSignup.mDOA);
                    PreferencesUtility.setServerDOB(LoginActivity.this, userSignup.mDOB);
                    PreferencesUtility.setGcmRegistered(LoginActivity.this, true);
                    PreferencesUtility.setGCMKey(LoginActivity.this, str);
                    if (!z) {
                        PreferencesUtility.setProfileFirstRun(LoginActivity.this, false);
                    }
                    Intent i = LoginActivity.this.getBaseContext().getPackageManager().getLaunchIntentForPackage(LoginActivity.this.getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    LoginActivity.this.startActivity(i);
                    return;
                }
                Toast.makeText(LoginActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                LoginActivity.this.finish();
            }

            public void failure() {
            }
        });
    }

    private String getUUid() {
        return Secure.getString(getContentResolver(), "android_id");
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
