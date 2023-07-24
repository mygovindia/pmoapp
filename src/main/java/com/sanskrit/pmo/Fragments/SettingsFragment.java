package com.sanskrit.pmo.Fragments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import android.util.Log;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.google.android.material.snackbar.Snackbar;
import com.sanskrit.pmo.Activities.MainActivity;
import com.sanskrit.pmo.Activities.NativeLoginActivity;
import com.sanskrit.pmo.Activities.VoiceOutActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.CommonFunctions;
import com.sanskrit.pmo.utils.Utils;
import com.squareup.okhttp.Cache;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@TargetApi(11)
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    private static final long CACHE_SIZE = 1048576;
    private static final String TAG =SettingsFragment.class.getName() ;
    Preference clearAllCache;
    Preference daydream;
    PreferenceCategory daydreamCategory;
    ListPreference language;
    Preference mygov;
    PreferenceScreen rootScreen;
    ListPreference themePreference;
    //Preference voiceout;
    SwitchPreference toggle_push;
    boolean isErrorOccur;
    boolean isPushchecked;
    private String deviceToken;
    private Dialog myDialog;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        PreferencesUtility.setOnSharedPreferenceChangeListener(getActivity(), this);
        this.themePreference = (ListPreference) findPreference("theme_preference");
        this.language = (ListPreference) findPreference("language_preference");
        this.daydreamCategory = (PreferenceCategory) findPreference("daydream_category");
        this.clearAllCache = findPreference("preference_clear_cache_all");
        this.mygov = findPreference("preference_account_mygov");
        this.toggle_push = (SwitchPreference) findPreference("toggle_push");

        this.daydream = findPreference("preference_daydream");
        this.daydream.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SettingsFragment.this.startActivity(new Intent("android.settings.DISPLAY_SETTINGS"));
                return true;
            }
        });

        if (!Utils.isJellyBeanMR1()) {
            getPreferenceScreen().removePreference(this.daydreamCategory);
        }
       // this.voiceout = findPreference("preference_voiceout");
//        this.voiceout.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                SettingsFragment.this.startActivity(new Intent(SettingsFragment.this.getActivity(), VoiceOutActivity.class));
//                return true;
//            }
//        });


        String divceId = PreferencesUtility.getUniqueDeviceId(getActivity());
        Log.d("divceId", "onCreate: " + divceId);


        toggle_push.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference,
                                              Object newValue) {
                boolean switched = ((SwitchPreference) preference).isChecked();

                isPushchecked = switched;
                if (Utils.isOnline(getActivity())) {
                    isErrorOccur = false;
                    if (switched && !isErrorOccur) {
                        new PMOPushNotificationsOnOffAsync().execute("0", "", "");
                        ;

                    } else {
                        new PMOPushNotificationsOnOffAsync().execute("1", "", "");
                        ;

                    }
                } else {
                    isErrorOccur = true;
                    CommonFunctions.ShowMessageToUser(getActivity(), CommonFunctions.NetworkMessage);
                    toggle_push.setChecked(isPushchecked);
                }

                return true;
            }

        });
        this.themePreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Intent i = SettingsFragment.this.getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(SettingsFragment.this.getActivity().getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                SettingsFragment.this.startActivity(i);
                return true;
            }
        });
        String languagePrefernce = PreferencesUtility.getLanguagePrefernce(getActivity());
        if (languagePrefernce.equals("bn")) {
            this.language.setDefaultValue("bengali");
            language.setValueIndex(3);
        } else if (languagePrefernce.equals("en")) {
            this.language.setDefaultValue("english");
            language.setValueIndex(0);
        } else if (languagePrefernce.equals("hi")) {
            this.language.setDefaultValue("hindi");
            language.setValueIndex(1);
        } else if (languagePrefernce.equals("asm")) {
            this.language.setDefaultValue("assamese");
            language.setValueIndex(2);
        } else if (languagePrefernce.equals("gu")) {
            this.language.setDefaultValue("gujarati");
            language.setValueIndex(4);
        } else if (languagePrefernce.equals("kn")) {
            this.language.setDefaultValue("kannada");
            language.setValueIndex(5);
        } else if (languagePrefernce.equals("ml")) {
            this.language.setDefaultValue("malayalam");
            language.setValueIndex(6);
        } else if (languagePrefernce.equals("mni")) {
            this.language.setDefaultValue("manipuri");
            language.setValueIndex(7);
        } else if (languagePrefernce.equals("mr")) {
            this.language.setDefaultValue("marathi");
            language.setValueIndex(8);
        } else if (languagePrefernce.equals("ory")) {
            this.language.setDefaultValue("odia");
            language.setValueIndex(9);
        } else if (languagePrefernce.equals("pa")) {
            this.language.setDefaultValue("punjabi");
            language.setValueIndex(10);
        } else if (languagePrefernce.equals("ta")) {
            this.language.setDefaultValue("tamil");
            language.setValueIndex(11);
        } else if (languagePrefernce.equals("te")) {
            this.language.setDefaultValue("telugu");
            language.setValueIndex(12);
        }/*else if (languagePrefernce.equals("ur")) {
            this.language.setDefaultValue("Urdu");
            language.setValueIndex(13);
        }*/ else {
            this.language.setDefaultValue("english");
            language.setValueIndex(0);
        }
        setDefaultLocale();
        this.language.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {


                String str = (String) newValue;

                if (str.equals("english")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "en");

                } else if (str.equals("gujarati")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "gu");

                } else if (str.equals("kannada")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "kn");

                } else if (str.equals("telugu")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "te");

                } else if (str.equals("bengali")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "bn");

                } else if (str.equals("odia")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "ory");

                } else if (str.equals("punjabi")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "pa");

                } else if (str.equals("hindi")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "hi");

                } else if (str.equals("assamese")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "asm");
                } else if (str.equals("tamil")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "ta");

                } else if (str.equals("marathi")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "mr");

                } else if (str.equals("malayalam")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "ml");

                } else if (str.equals("manipuri")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "mni");
                }/*else if (str.equals("urdu")) {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "ur");
                } */ else {
                    PreferencesUtility.setLanguagePreference(SettingsFragment.this.getActivity(), "en");

                }

                SettingsFragment.this.setDefaultLocale();
                Intent i = SettingsFragment.this.getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(SettingsFragment.this.getActivity().getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SettingsFragment.this.startActivity(i);
                return true;
            }
        });
        this.clearAllCache.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Reservoir.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    new Cache(SettingsFragment.this.getActivity().getApplicationContext().getCacheDir(), 1048576).delete();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (SettingsFragment.this.getView() != null) {
                    Snackbar.make(SettingsFragment.this.getView(), (int) R.string.all_cache_cleared, Snackbar.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsFragment.this.getActivity(), R.string.all_cache_cleared, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        if (PreferencesUtility.getMygovLoggedIn(getActivity())) {
            this.mygov.setTitle(R.string.logout_mygov);
            this.mygov.setSummary(getActivity().getString(R.string.logged_in_as) + " " + PreferencesUtility.getMygovOauthName(getActivity()));
        } else {
            this.mygov.setTitle(getActivity().getString(R.string.account_mygov));
            this.mygov.setSummary(getActivity().getString(R.string.login_to_mygov));
        }
        this.mygov.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (PreferencesUtility.getMygovLoggedIn(SettingsFragment.this.getActivity())) {
                    PreferencesUtility.clearMygovCredentials(SettingsFragment.this.getActivity());
                    if (SettingsFragment.this.getView() != null) {
                        Snackbar.make(SettingsFragment.this.getView(), (int) R.string.success_logout_mygov, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingsFragment.this.getActivity(), R.string.success_logout_mygov, Toast.LENGTH_SHORT).show();
                    }
                    SettingsFragment.this.mygov.setTitle(SettingsFragment.this.getActivity().getString(R.string.account_mygov));
                    SettingsFragment.this.mygov.setSummary(SettingsFragment.this.getActivity().getString(R.string.login_to_mygov));
                } else {
                    SettingsFragment.this.startActivity(new Intent(SettingsFragment.this.getActivity(), NativeLoginActivity.class).setAction("MyGov"));
                    SettingsFragment.this.getActivity().finish();
                }
                return true;
            }
        });
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

    private void setDefaultLocale() {
        try {
            Locale locale = new Locale(PreferencesUtility.getLanguagePrefernce(getActivity()), "IN");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class PMOPushNotificationsOnOffAsync extends AsyncTask<String, Void, String> {
        final String TAG = "";
        final OkHttpClient client;
        String onOff;

        private PMOPushNotificationsOnOffAsync() {
            this.client = CommonFunctions.HtppcallforInterprator();
            //myDialog = CommonFunctions.showDialog();
            myDialog = CommonFunctions.showDialog(getActivity());

        }

        protected void onPreExecute() {
            super.onPreExecute();
            /*myDialog.show();
            myDialog.setCancelable(true);
            myDialog.setCanceledOnTouchOutside(false);*/
            myDialog.show();
            myDialog.setCancelable(true);
            myDialog.setCanceledOnTouchOutside(false);
            deviceToken = PreferencesUtility.getFCMID(getActivity());
        }

        protected String doInBackground(String... params) {

            try {
                onOff = params[0];
                FormBody.Builder add = new FormBody.Builder()
                        .add("imei", PreferencesUtility.getUniqueDeviceId(getActivity()))
                        .add("device_type", "android")
                        .add("notification", onOff)
                        .add("device_token", deviceToken);
                Response execute = this.client.newCall(new Request.Builder()
                        .url("https://api.pmindia.gov.in/notification-settings")
                        .addHeader("Auth", "6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222")
                        .post(add.build()).build()).execute();
                                if (execute.isSuccessful()) {
                    String response = execute.body().string();
                    Log.v(TAG, "LOGIN_RESPONSE: " + response);
                    try {
                        JSONObject jSONObject = new JSONObject(execute.body().string());
                        Log.v(TAG, "LOGIN_RESPONSE: " + jSONObject);
                        response = jSONObject.toString();
                    } catch (JSONException e) {
                        Log.v(TAG, "JSONException: " + e.getMessage());
                    } catch (Exception e) {
                        Log.v(TAG, "Exception: " + e.getMessage());
                    }
                    return response;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (myDialog != null && myDialog.isShowing()) myDialog.dismiss();
            if (result != null && result.trim().length() > 0 && ("Success Inserted".equalsIgnoreCase(getResult(result))
                    || "Success Inserted!".equalsIgnoreCase(getResult(result))
                    || "Success Updated".equalsIgnoreCase(getResult(result))
                    || "Success Updated!".equalsIgnoreCase(getResult(result)))) {

                isErrorOccur = false;
                PreferencesUtility.setPrefNotificationOnOf(getActivity(), onOff);
                Toast.makeText(getActivity(), getResult(result), Toast.LENGTH_SHORT).show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //builder.setTitle(getString(R.string.unable_to_connect));
                builder.setCancelable(false);
                Log.d(TAG, "onPostExecute: " + result);
                builder.setMessage(getString(R.string.unable_to_connect));
                builder.setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        toggle_push.setChecked(isPushchecked);
                        isErrorOccur = true;
                        if (isPushchecked)
                            PreferencesUtility.setPrefNotificationOnOf(getActivity(), "1");
                        else
                            PreferencesUtility.setPrefNotificationOnOf(getActivity(), "0");

                    }
                });
                builder.show();

            }
        }

        private String getResult(String response) {
            return response.substring(1, response.length() - 1);
        }
    }
}
