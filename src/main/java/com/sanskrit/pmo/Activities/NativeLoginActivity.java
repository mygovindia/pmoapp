package com.sanskrit.pmo.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.material.textfield.TextInputLayout;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.CommonFunctions;
import com.sanskrit.pmo.network.server.SanskritClient;
import com.sanskrit.pmo.network.server.callbacks.RequestTokenListener;
import com.sanskrit.pmo.network.server.callbacks.UserLoginListener;
import com.sanskrit.pmo.network.server.callbacks.UserSignupListener;
import com.sanskrit.pmo.network.server.models.RequestToken;
import com.sanskrit.pmo.network.server.models.UserLogin;
import com.sanskrit.pmo.network.server.models.UserSignup;
import com.sanskrit.pmo.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLHandshakeException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;


public class NativeLoginActivity extends AppCompatActivity {

    private static final int REQUEST_MULTIPLE_PERMISSION = 101;
    Toolbar toolbar;
    Button buttonLogin, btnResend;
    GoogleCloudMessaging gcmObj;
    String regId = null;
    RelativeLayout relativeLayoutSignup;
    String phoneNumber;
    BroadcastReceiver receiver = new SMSBroadcast();
    TextInputLayout inputLayoutOTP, inputLayoutMobile;
    Dialog myDialog;
    String loginToken, refreshToken;
    private EditText editLogin;
    private EditText editOTP;
    private TextView textViewLogin;
    private String langCode;
    private String deviceToken;
    private String IMEI;
    private TelephonyManager telephonyManager;

    public static Boolean isValidInteger(String str) {
        try {
            if (Long.valueOf(str.toString().trim()) != null) {
                return Boolean.valueOf(true);
            }
            return Boolean.valueOf(false);
        } catch (NumberFormatException e) {
            return Boolean.valueOf(false);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_login);
        initView();

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        langCode = PreferencesUtility.getLanguagePrefernce(NativeLoginActivity.this);

        //SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        //deviceToken = pref.getString("regId", null);
        deviceToken = PreferencesUtility.getFCMID(getApplicationContext());
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        textViewLogin = findViewById(R.id.tv_login);
        editLogin = findViewById(R.id.edt_login);
        inputLayoutOTP = findViewById(R.id.ti_otp);
        inputLayoutMobile = findViewById(R.id.ti_mobile);

        editOTP = findViewById(R.id.edt_otp);
        buttonLogin = findViewById(R.id.btn_login);
        btnResend = findViewById(R.id.btn_resend);
        relativeLayoutSignup = findViewById(R.id.relative_signup);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.login));
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        relativeLayoutSignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NativeLoginActivity.this, NativeSignupActivity.class));
            }
        });

        btnResend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isOnline(NativeLoginActivity.this)) {
                    btnResend.setVisibility(View.GONE);
                    buttonLogin.setEnabled(true);
                    buttonLogin.setAlpha(1f);
//                    if (TextUtils.isEmpty(NativeLoginActivity.this.editLogin.getText().toString().trim())) {
//                        CommonFunctions.ShowMessageToUser(NativeLoginActivity.this, "Enter your OTP");
//                        return;
//                    }
                    new GetOtpforLogin().execute(new String[]{"0"});
                } else {
                    CommonFunctions.ShowMessageToUser(NativeLoginActivity.this, CommonFunctions.NetworkMessage);
                }
            }
        });
    }

    public void onLoginBtn(View view) {
        if (Utils.isOnline(NativeLoginActivity.this)) {

            if (buttonLogin.getText().toString().equalsIgnoreCase(getString(R.string.submit_otp)) && editOTP.getText().toString().trim().length() >= 6) {

                new GetToken().execute(new String[]{"0"});
            } else if (!buttonLogin.getText().toString().equalsIgnoreCase(getString(R.string.submit_otp))) {
                String getPnumber = editLogin.getText().toString().trim();
                if (TextUtils.isEmpty(getPnumber)) {
                    CommonFunctions.ShowMessageToUser(NativeLoginActivity.this, getString(R.string.please_enter_mobile_number_or_email_Id));
                    return;
                } else if (!isValidInteger(getPnumber).booleanValue()) {
                    if (getPnumber.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                        phoneNumber = editLogin.getText().toString();
                        new GetOtpforLogin().execute(new String[]{"0"});
                        return;
                    }
                    Toast.makeText(NativeLoginActivity.this.getApplicationContext(), R.string.please_enter_valid_email_id, Toast.LENGTH_SHORT).show();
                    return;
                } else if (getPnumber.length() > 15 || getPnumber.length() < 5) {
                    CommonFunctions.ShowMessageToUser(NativeLoginActivity.this, getString(R.string.please_enter_mobile_number));
                    return;
                } else if (Patterns.PHONE.matcher(getPnumber).matches()) {
                    phoneNumber = editLogin.getText().toString();
                    new GetOtpforLogin().execute(new String[]{"0"});
                    return;
                } else {

                    CommonFunctions.ShowMessageToUser(NativeLoginActivity.this, getString(R.string.please_enter_mobile_number));
                    return;
                }


            } else {
                CommonFunctions.ShowMessageToUser(NativeLoginActivity.this, getString(R.string.please_enter_valid_otp_number));

            }
        } else {
            CommonFunctions.ShowMessageToUser(NativeLoginActivity.this, CommonFunctions.NetworkMessage);
        }

    }

    private void attemptServerLogin(final String email, final String name) {
        SanskritClient.getInstance(this).getRequestToken(Utils.getPermaToken(this), new RequestTokenListener() {
            public void success(RequestToken token) {
                if (token.mErrorResponse == null) {
                    NativeLoginActivity.this.checkUserExistsOnServer(token.mToken, email, name);
                } else {
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    Log.d("lol11", "errorrrrr");
                }
            }

            public void failure() {

                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
            }
        });
    }

    private void checkUserExistsOnServer(String token, final String email, final String name) {
        SanskritClient.getInstance(this).loginUser(token, email, "", new UserLoginListener() {
            public void success(UserLogin userLogin) {
                if (userLogin == null) {
                    Toast.makeText(NativeLoginActivity.this, R.string.some_error_occurred, Toast.LENGTH_SHORT).show();
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    NativeLoginActivity.this.finish();
                } else if (userLogin.mErrorResponse == null) {
                    NativeLoginActivity.this.registerToGcmInBackground(email, name, false, userLogin.mUUID, userLogin);
                } else if (userLogin.mErrorCode == 10) {
                    Log.d("server", "error attepting to check on database");
                } else if (userLogin.mErrorCode == 100) {
                    NativeLoginActivity.this.registerToGcmInBackground(email, name, true, NativeLoginActivity.this.getUUid(), null);
                }
            }

            public void failure() {
            }
        });
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
                        if (NativeLoginActivity.this.gcmObj == null) {
                            NativeLoginActivity.this.gcmObj = GoogleCloudMessaging.getInstance(NativeLoginActivity.this);
                        }
                        NativeLoginActivity.this.regId = NativeLoginActivity.this.gcmObj.register("339180625670");
                        return "Registration ID :" + NativeLoginActivity.this.regId;
                    }

                } catch (IOException ex) {
                    return "Error :" + ex.getMessage();
                }
            }

            protected void onPostExecute(String msg) {
                if (NativeLoginActivity.this.regId != null) {
                    NativeLoginActivity.this.getAccessTokenAndSignup(NativeLoginActivity.this.regId, str, str2, z, str3, userLogin2);
                    Log.d("server", "registered with gcm");
                    return;
                }
                NativeLoginActivity.this.getAccessTokenAndSignup("", str, str2, z, str3, userLogin2);
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
                    NativeLoginActivity.this.signUp(token.mToken, str, str2, str3, z, str4, userLogin2);
                } else {
                    Log.d("lol11", "errorrrrr");
                    if (myDialog != null) {
                        myDialog.dismiss();
                    }
                }
            }

            public void failure() {
                if (myDialog != null) {
                    myDialog.dismiss();
                }
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
                if (userSignup.mErrorResponse == null) {
                    PreferencesUtility.setFirstRun(NativeLoginActivity.this, false);
                    PreferencesUtility.setServerUuid(NativeLoginActivity.this, userSignup.uuid);
                    PreferencesUtility.setServerDOA(NativeLoginActivity.this, userSignup.mDOA);
                    PreferencesUtility.setServerDOB(NativeLoginActivity.this, userSignup.mDOB);
                    PreferencesUtility.setGcmRegistered(NativeLoginActivity.this, true);
                    PreferencesUtility.setGCMKey(NativeLoginActivity.this, str);
                    if (!z) {
                        PreferencesUtility.setProfileFirstRun(NativeLoginActivity.this, false);
                    }

                    Intent i = NativeLoginActivity.this.getBaseContext().getPackageManager().getLaunchIntentForPackage(NativeLoginActivity.this.getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    NativeLoginActivity.this.startActivity(i);
                    if (myDialog != null) {
                        myDialog.dismiss();
                    }
                    return;
                }
                Toast.makeText(NativeLoginActivity.this, R.string.some_error_occurred, Toast.LENGTH_SHORT).show();
                if (myDialog != null) {
                    myDialog.dismiss();
                }
                NativeLoginActivity.this.finish();

            }

            public void failure() {
                if (myDialog != null) {
                    myDialog.dismiss();
                }
            }
        });
    }

    private String getUUid() {
        return Secure.getString(getContentResolver(), "android_id");
    }

    private String getFilteredHindiMessage(String message, String langCode) {
        if (message == null) return message;
        if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeLoginActivity.this, R.string.error_email_confirmation, "en"))) {
            message = Utils.getStringByLocale(NativeLoginActivity.this, R.string.error_email_confirmation, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeLoginActivity.this, R.string.otp_sent_mobile_email_success, "en"))) {
            message = Utils.getStringByLocale(NativeLoginActivity.this, R.string.otp_sent_mobile_email_success, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeLoginActivity.this, R.string.otp_sent_mobile_success, "en"))) {
            message = Utils.getStringByLocale(NativeLoginActivity.this, R.string.otp_sent_mobile_success, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeLoginActivity.this, R.string.otp_sent_mobile_failed, "en"))) {
            message = Utils.getStringByLocale(NativeLoginActivity.this, R.string.otp_sent_mobile_failed, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeLoginActivity.this, R.string.error_mobile_confirmation, "en"))) {
            message = Utils.getStringByLocale(NativeLoginActivity.this, R.string.error_mobile_confirmation, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeLoginActivity.this, R.string.otp_sent_email_success, "en"))) {
            message = Utils.getStringByLocale(NativeLoginActivity.this, R.string.otp_sent_email_success, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeLoginActivity.this, R.string.error_user_not_registered, "en"))) {
            message = Utils.getStringByLocale(NativeLoginActivity.this, R.string.error_user_not_registered, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeLoginActivity.this, R.string.unable_to_connect, "en"))) {
            message = Utils.getStringByLocale(NativeLoginActivity.this, R.string.unable_to_connect, langCode);
        }
        Log.v("CHANGED_MSG", message);
        return message;
    }

    public void onResume() {
        super.onResume();
        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(this.receiver, new IntentFilter("otp"));
        } catch (Exception e) {
        }
       /* if (checkPermissionStatus()) {
            getDeviceDetails();
        }*/
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiver);
    }

    class GetOtpforLogin extends AsyncTask<String, Void, String> {
        final OkHttpClient client;
        final Dialog myDialog;
        String message;
        String poss;
        String status;
        boolean isSSLException;

        private GetOtpforLogin() {
            this.client = CommonFunctions.HtppcallforGet();
            this.myDialog = CommonFunctions.showDialog(NativeLoginActivity.this);
            this.status = "";
            this.message = getString(R.string.unable_to_connect);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            try {
                this.myDialog.show();
                this.myDialog.setCancelable(true);
                this.myDialog.setCanceledOnTouchOutside(false);
            } catch (Exception e) {
            }
        }

        protected String doInBackground(String... strArr) {
            try {
                this.poss = strArr[0];
                String str = "https://auth.mygov.in/native/login/otp";
                try {
                    Response execute = this.client.newCall(new Builder().url(str).post(new FormBody.Builder().add("username", phoneNumber).build()).build()).execute();
                    if (execute.isSuccessful()) {
                        JSONObject jSONObject = new JSONObject(execute.body().string());
                        this.status = jSONObject.getString("status");
                        this.message = jSONObject.getString("message");
                    }
                } catch (SSLHandshakeException e3) {
                    Log.e("SSLHandshakeException", "SSLHandshakeException: " + e3.getMessage());
                    this.isSSLException = true;

                } catch (IOException e) {

                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (myDialog != null && myDialog.isShowing()) myDialog.dismiss();
            try {
                if (this.status.equals("success") && this.poss.equals("0")) {


                    textViewLogin.setText(R.string.OTP_has_been_sent_to_your_email_mobile);

                    buttonLogin.setText(getString(R.string.submit_otp));

                    inputLayoutMobile.setVisibility(View.GONE);
                    inputLayoutOTP.setVisibility(View.VISIBLE);
                    //  editLogin.setText("");
                    //editLogin.getText().clear();
                    //editLogin.setHint("Enter your OTP");

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnResend.setVisibility(View.VISIBLE);
                                    buttonLogin.setEnabled(true);
                                    //buttonLogin.setAlpha(0.5f);
                                }
                            });
                        }
                    }, 60000);
                } else if (this.poss.equals("0")) {
                    if (isSSLException) {
                        new GetOtpforLogin().execute(new String[]{"0"});
                    }
                }

                if (langCode.equalsIgnoreCase("hi")) {
                    this.message = getFilteredHindiMessage(this.message != null ? this.message.trim() : null, langCode);
                }
                if (!isSSLException)
                    Toast.makeText(NativeLoginActivity.this, this.message, Toast.LENGTH_LONG).show();

            } catch (Exception e) {
            }
        }

    }

    class GetToken extends AsyncTask<String, Void, String> {
        final OkHttpClient client;
        boolean check;
        boolean isSSLException;


        private GetToken() {
            this.client = CommonFunctions.HtppcallforGetLogin();
            myDialog = CommonFunctions.showDialog(NativeLoginActivity.this);
            this.check = true;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            myDialog.show();
            myDialog.setCancelable(true);
            myDialog.setCanceledOnTouchOutside(false);
        }

        protected String doInBackground(String... strArr) {
            try {
                String str = strArr[0];
                String str2 = "https://auth.mygov.in/oauth2/token";
                try {
                    Response execute = this.client.newCall(new Builder().url(str2).post(new FormBody.Builder().add("grant_type", "password").add("client_id", getString(R.string.client_id)).add("client_secret", getString(R.string.secret_key)).add("username", phoneNumber).add("password", editOTP.getText().toString().trim()).add("scope", "user_profile").build()).build()).execute();
                    if (execute.isSuccessful()) {
                        try {
                            JSONObject jSONObject = new JSONObject(execute.body().string());
                            Log.e("", "doInBackground: " + jSONObject);
                            //  str = jSONObject.getString("access_token");
                            loginToken = jSONObject.getString("access_token");
                            refreshToken = jSONObject.getString("refresh_token");
                           /* SanskritCallbackClient.getInstance(NativeLoginActivity.this).getUserProfile(token, new UserProfileListener() {
                                public void success(UserProfileMygov userProfile) {
                                    if (userProfile != null) {
                                        PreferencesUtility.setMygovAuthToken(NativeLoginActivity.this, token, userProfile.mFullName, userProfile.mEmail, refreshToken, true);
                                        NativeLoginActivity.this.attemptServerLogin(userProfile.mEmail, userProfile.mFullName);

                                        return;
                                    }
                                    Log.d("lol", "userprofile is null!!!");
                                    Toast.makeText(NativeLoginActivity.this, R.string.error_fetching_profile, Toast.LENGTH_SHORT).show();
                                    if(myDialog!=null)
                                    {
                                        myDialog.dismiss();
                                    }
                                    NativeLoginActivity.this.finish();
                                }

                                public void failure() {
                                    Toast.makeText(NativeLoginActivity.this, R.string.error_fetching_profile, Toast.LENGTH_SHORT).show();
                                    if(myDialog!=null)
                                    {
                                        myDialog.dismiss();
                                    }
                                    NativeLoginActivity.this.finish();
                                }
                            });*/
                        } catch (JSONException e2) {
                            this.check = false;
                        }
                    } else {
                        this.check = false;
                    }
                } catch (SSLHandshakeException e3) {
                    Log.e("SSLHandshakeException", "SSLHandshakeException: " + e3.getMessage());
                    this.check = false;
                    this.isSSLException = true;

                } catch (IOException e3) {
                    this.check = false;
                }
            } catch (Exception e4) {
                this.check = false;
            }
            return null;
        }

        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            if (this.check) {
                new FetchProfile().execute(new String[]{loginToken});
                //finish();
            } else {
                if (myDialog != null && myDialog.isShowing()) myDialog.dismiss();
                if (isSSLException) {
                    new GetToken().execute(new String[]{"0"});
                } else {
                    CommonFunctions.ShowMessageToUser(NativeLoginActivity.this, getString(R.string.otp_verification_failed));
                }

            }
            // myDialog.dismiss();
        }

    }



/*    private boolean checkPermissionStatus() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionReadPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionReadPhoneState != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_MULTIPLE_PERMISSION);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }*/

    /* @Override
     public void onRequestPermissionsResult(int requestCode,
                                            String permissions[], int[] grantResults) {
         switch (requestCode) {
             case REQUEST_MULTIPLE_PERMISSION: {
                 Map<String, Integer> perms = new HashMap<>();
                 perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                 if (grantResults.length > 0) {
                     for (int i = 0; i < permissions.length; i++)
                         perms.put(permissions[i], grantResults[i]);
                     if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                         getDeviceDetails();
                     } else {
                         Toast.makeText(NativeLoginActivity.this, getString(R.string.enable_permission), Toast.LENGTH_SHORT).show();
                     }
                 }
                 return;
             }
         }
     }*/
   /* @SuppressLint("MissingPermission")
    private void getDeviceDetails() {
        IMEI = telephonyManager.getDeviceId();
        if (IMEI == null || IMEI.length() == 0) {
            IMEI = "000000000000000";
        }
        Log.v(TAG, "IMEI: " + IMEI);
        PreferencesUtility.setIMEI(NativeLoginActivity.this, IMEI);
    }
*/
    class SMSBroadcast extends BroadcastReceiver {
        SMSBroadcast() {
        }

        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equalsIgnoreCase("otp")) {
                    Object substring = CommonFunctions.stripNonDigits(intent.getStringExtra("message")).substring(0, 6);
                    editLogin.setText(substring.toString());
                    editLogin.setSelection(substring.toString().length());
                    if (CommonFunctions.cbuilder1 != null) {
                        AlertDialog create = CommonFunctions.cbuilder1.create();
                        if (create.isShowing()) {
                            create.dismiss();
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    class FetchProfile extends AsyncTask<String, Void, String> {
        final OkHttpClient client;
        boolean check;
        String fullName;
        String password;
        String mEmail;
        String number;

        private FetchProfile() {
            this.client = CommonFunctions.HtppcallforInterprator();
            //myDialog = CommonFunctions.showDialog(NativeLoginActivity.this);
            this.check = true;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            /*myDialog.show();
            myDialog.setCancelable(true);
            myDialog.setCanceledOnTouchOutside(false);*/
        }

        protected String doInBackground(String... strArr) {
            try {
                String str = strArr[0];
                try {
                    FormBody.Builder add = new FormBody.Builder()
                            .add("access_token", str);
                    Response execute = this.client.newCall(new Builder()
                            .url("https://auth.mygov.in/pmoappmygov/user/profile")
                            .post(add.build()).build())
                            .execute();
                    if (execute.isSuccessful()) {
                        JSONObject jSONObject = new JSONObject(execute.body().string());
                        Log.e("FetchProfile", "doInBackground: profile" + jSONObject);
                        String mFullName = "";

                        mEmail = jSONObject.getString("mail");
                        if(mEmail==null || mEmail.equals("null"))
                            mEmail="";
                        number = jSONObject.getJSONObject("sms_user").getString("number");

                        try {
                            mFullName = jSONObject.getJSONObject("field_full_name").getJSONArray("und").getJSONObject(0).getString("value");
                        } catch (Exception e) {
                            mFullName = "";
                            e.printStackTrace();
                        }
                        if (!(mFullName.trim().length() > 0)) {
                            mFullName = jSONObject.getString("name");

                        }

                        fullName = mFullName;
                        password = editOTP.getText().toString().trim();

                        PreferencesUtility.setMygovAuthToken(NativeLoginActivity.this, loginToken, mFullName, mEmail, refreshToken, true);
                        this.check = true;
                        //NativeLoginActivity.this.attemptServerLogin(mEmail, mFullName);
                        /*if (myDialog != null) {
                            myDialog.dismiss();
                        }
                        Intent intent = new Intent(NativeLoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
                    } /*else {
                        check = false;
                        if (myDialog != null) {
                            myDialog.dismiss();
                        }
                        Intent intent = new Intent(NativeLoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }*/
                } catch (Exception e) {
                    check = false;
                    e.printStackTrace();
                    return null;

                }

                return null;
            } catch (Exception e) {
                check = false;
                e.printStackTrace();

            }
            return null;
        }

        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (this.check) {
                PMLoginAsync async = new PMLoginAsync();
                async.execute(number, password, fullName);
            } else {
                if (myDialog != null && myDialog.isShowing()) myDialog.dismiss();
                CommonFunctions.ShowMessageToUser(NativeLoginActivity.this, getString(R.string.unable_to_connect));

            }
        }

    }

    class PMLoginAsync extends AsyncTask<String, Void, String> {
        final String TAG = PMLoginAsync.class.getSimpleName();
        final OkHttpClient client;
        String mobileNumber;
        String password;
        String fullName;
        boolean status=true;

        private PMLoginAsync() {
            this.client = CommonFunctions.HtppcallforInterprator();
            //myDialog = CommonFunctions.showDialog(NativeLoginActivity.this);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            /*myDialog.show();
            myDialog.setCancelable(true);
            myDialog.setCanceledOnTouchOutside(false);*/
            if (myDialog != null && !myDialog.isShowing()) myDialog.show();
        }

        protected String doInBackground(String... params) {
            try {
                mobileNumber = params[0];
                password = params[1];
                fullName = params[2];
                FormBody.Builder add = new FormBody.Builder()
                        .add("email", PreferencesUtility.getMygovOauthEmail(NativeLoginActivity.this))
                        .add("mobile", mobileNumber)
                        .add("notification", PreferencesUtility.getPrefNotificationOnOf(NativeLoginActivity.this))
                        .add("password", password)
                        .add("imei", PreferencesUtility.getUniqueDeviceId(NativeLoginActivity.this))
                        .add("device_type", "android")
                        .add("device_token", deviceToken);
                Response execute = this.client.newCall(new Builder()
                        .url("https://api.pmindia.gov.in/mobile_login_insert")
                        .addHeader("Auth", "6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222")
                        .post(add.build()).build()).execute();
                if (execute.isSuccessful()) {
                    String response = execute.body().string();
                    status=true;
//                    Log.v(TAG, "LOGIN_RESPONSE: " + response);
//                    try {
//                        JSONObject jSONObject = new JSONObject(execute.body().string());
//                        Log.v(TAG, "LOGIN_RESPONSE: " + jSONObject);
//                        response = jSONObject.toString();
//                    } catch (JSONException e) {
//                        Log.v(TAG, "JSONException: " + e.getMessage());
//                    } catch (Exception e) {
//                        Log.v(TAG, "Exception: " + e.getMessage());
//                    }
                    return response;
                }
            } catch (Exception e) {
                status=false;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (myDialog != null && myDialog.isShowing()) myDialog.dismiss();
            if (status) {
                //NativeLoginActivity.this.attemptServerLogin(email, fullName);
                PreferencesUtility.setPrefPrefMobileNumber(NativeLoginActivity.this, mobileNumber);
                Intent intent = new Intent(NativeLoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NativeLoginActivity.this);
                    //builder.setTitle(getString(R.string.unable_to_connect));
                    builder.setCancelable(false);
                    builder.setMessage(getString(R.string.unable_to_connect));
                    builder.setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PMLoginAsync async = new PMLoginAsync();
                            async.execute(mobileNumber, password, fullName);
                        }
                    });
                    builder.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        private String getResult(String response) {
            return response.substring(1, response.length() - 1);
        }

    }

}
