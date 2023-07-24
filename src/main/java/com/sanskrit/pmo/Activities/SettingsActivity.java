package com.sanskrit.pmo.Activities;

import android.os.Bundle;

import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.sanskrit.pmo.Fragments.SettingsFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;


public class SettingsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        if (PreferencesUtility.getTheme(this).equals("dark")) {
            setTheme(R.style.AppThemeDark);
        } else if (PreferencesUtility.getTheme(this).equals("black")) {
            setTheme(R.style.AppThemeBlack);
        } else
            setTheme(R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_settings);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((int) R.string.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
