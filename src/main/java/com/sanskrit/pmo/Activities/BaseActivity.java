package com.sanskrit.pmo.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import com.crashlytics.android.Crashlytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.TViewUtil;
import com.sanskrit.pmo.player.MusicService;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    public MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    public boolean isLightTheme;
    public boolean isDarkTheme;
    private boolean fullscreen = false;
    public boolean notifMenuVisible = false;
    public boolean langMenuVisible = false;
    public boolean settingMenuVisible = false;
    public boolean searchMenuVisible = true;
  //  boolean bound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isLightTheme = PreferencesUtility.getTheme(this).equals("light");
        isDarkTheme = PreferencesUtility.getTheme(this).equals("dark");

        if (isLightTheme) {
            if (fullscreen)
                setTheme(R.style.AppTheme_FullScreen_Light);
            else setTheme(R.style.AppThemeLight);
        } else if
        (isDarkTheme) {
            if (fullscreen)
                setTheme(R.style.AppTheme_FullScreen_Dark);
            else setTheme(R.style.AppThemeDark);
        } else {
            if (fullscreen)
                setTheme(R.style.AppTheme_FullScreen_Black);
            else setTheme(R.style.AppThemeBlack);
        }

        super.onCreate(savedInstanceState);
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicSrv = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicConnection=null;
            musicBound = false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            if (langMenuVisible) {
                menu.findItem(R.id.action_language_selection).setVisible(true);
            } else menu.findItem(R.id.action_language_selection).setVisible(false);
            if (notifMenuVisible) {
                menu.findItem(R.id.action_notification).setVisible(true);
            } else menu.findItem(R.id.action_notification).setVisible(false);
            if (settingMenuVisible) {
                menu.findItem(R.id.action_settings).setVisible(true);
                menu.findItem(R.id.action_feedback).setVisible(true);
            }

            if (searchMenuVisible) {
                menu.findItem(R.id.action_search).setVisible(true);
            } else menu.findItem(R.id.action_search).setVisible(false);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
           // Crashlytics.logException(e);
            e.printStackTrace();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent1 = new Intent(BaseActivity.this, SettingsActivity.class);
                startActivity(intent1);
                return true;
            case R.id.action_feedback:
                startActivity(new Intent(BaseActivity.this, FeedbackActivity.class));
                break;
            case R.id.action_search:
                startActivity(new Intent(BaseActivity.this, SearchActivity.class));
                break;
            case R.id.action_notification:
                startActivity(new Intent(BaseActivity.this, NotificationsActivity.class));
                break;
            case R.id.action_language_selection:
                showLanguageSelectionDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLanguageSelectionDialog() {
        final String languageEntry[] = getResources().getStringArray(R.array.pref_language_entries);
        final String languageValues[] = getResources().getStringArray(R.array.pref_language_values);


        int index = getSelectedLanguageIndex();

        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this)
                .setTitle(getString(R.string.language))
                .setSingleChoiceItems(languageEntry, index, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setLanguagePreferences(languageValues[which]);
                        dialog.dismiss();
                    }
                }).setNegativeButton(getText(R.string.cancel), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();


    }

    private int getSelectedLanguageIndex() {
        int index = 0;
        String languagePrefernce = PreferencesUtility.getLanguagePrefernce(BaseActivity.this);

        if (languagePrefernce.equals("en")) {
            index = 0;
        } else if (languagePrefernce.equals("hi")) {
            index = 1;
        } else if (languagePrefernce.equals("asm")) {
            index = 2;
        } else if (languagePrefernce.equals("bn")) {
            index = 3;
        } else if (languagePrefernce.equals("gu")) {
            index = 4;
        } else if (languagePrefernce.equals("kn")) {
            index = 5;
        } else if (languagePrefernce.equals("ml")) {
            index = 6;
        } else if (languagePrefernce.equals("mni")) {
            index = 7;
        } else if (languagePrefernce.equals("mr")) {
            index = 8;
        } else if (languagePrefernce.equals("ory")) {
            index = 9;
        } else if (languagePrefernce.equals("pa")) {
            index = 10;
        } else if (languagePrefernce.equals("ta")) {
            index = 11;
        } else if (languagePrefernce.equals("te")) {
            index = 12;
        } /*else if (languagePrefernce.equals("ur")) {
            index = 13;
        } */ else {
            index = 0;

        }
        return index;
    }

    private void setLanguagePreferences(String lang) {
        if (lang.equalsIgnoreCase("english")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "en");

        } else if (lang.equalsIgnoreCase("gujarati")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "gu");
        } else if (lang.equalsIgnoreCase("kannada")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "kn");
        } else if (lang.equalsIgnoreCase("telugu")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "te");
        } else if (lang.equalsIgnoreCase("bengali")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "bn");
        } else if (lang.equalsIgnoreCase("odia")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "ory");
        } else if (lang.equalsIgnoreCase("punjabi")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "pa");
        } else if (lang.equalsIgnoreCase("hindi")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "hi");
        } else if (lang.equalsIgnoreCase("assamese")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "asm");
        } else if (lang.equalsIgnoreCase("tamil")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "ta");
        } else if (lang.equalsIgnoreCase("marathi")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "mr");
        } else if (lang.equalsIgnoreCase("malayalam")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "ml");
        } else if (lang.equalsIgnoreCase("manipuri")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "mni");
        }/*else if (lang.equalsIgnoreCase("urdu")) {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "ur");

        }*/ else {
            PreferencesUtility.setLanguagePreference(BaseActivity.this, "en");
        }
        setDefaultLocale();
        Intent intent = new Intent(this, LanguageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setDefaultLocale() {
        try {
            Locale locale = new Locale(PreferencesUtility.getLanguagePrefernce(BaseActivity.this), "IN");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(playIntent);
//            } else {
//                startService(playIntent);
//            }
           // startService(playIntent);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service.
        if (musicBound) {
            unbindService(musicConnection);
            musicBound = false;
        }
    }

    @Override
    protected void onDestroy() {
       // stopService(playIntent);

        //Comment by me
        try {
            if (musicConnection != null)
                getApplicationContext().unbindService(musicConnection);
            musicSrv = null;
        }catch (IllegalArgumentException  e){

        }
        super.onDestroy();
    }

    public void showSwipeRefresh(final SwipeRefreshLayout swipeRefresh) {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    public void hideSwipeRefresh(final SwipeRefreshLayout swipeRefresh) {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    public void setEmptyView(RecyclerView recyclerView) {
        TViewUtil.EmptyViewBuilder.getInstance(this)
                .setEmptyText("NO DATA")
                .setShowText(true)
                .setShowIcon(false)
                .setIconSrc(R.drawable.ic_error_outline_black_24dp)
                .bindView(recyclerView);
    }
}