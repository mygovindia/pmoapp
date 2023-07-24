package com.sanskrit.pmo.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.sanskrit.pmo.Fragments.LanguageSelectionFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultLocale(PreferencesUtility.getLanguagePrefernce(this));
        if (PreferencesUtility.getFirstRun(this)) {
            setContentView(R.layout.activity_setup_fragment_container);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LanguageSelectionFragment()).commit();
        } else if (PreferencesUtility.getProfileFirstRun(this)) {
            startActivity(new Intent(this, AppSetupActivity.class));
            finish();
        } else {

            startActivity(new Intent(this, MainActivity.class));

            finish();
        }
    }

    private void setDefaultLocale(String localeString) {
        Locale locale = new Locale(localeString);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
