package com.sanskrit.pmo.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.sanskrit.pmo.Activities.AppSetupActivity;
import com.sanskrit.pmo.R;


public class ProfilePreferenceFragment extends PreferenceFragment {
    Preference update;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.profile_preference);
        // this.toggleLocation = (SwitchPreference) findPreference("toggle_location_push");
        this.update = findPreference("preference_profile_update");
      /*  this.toggleLocation.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (((SwitchPreference) preference).isChecked()) {
                    LocationUtils.getInstance(ProfilePreferenceFragment.this.getActivity()).updateUserLocation();
                } else {
                    LocationUtils.getInstance(ProfilePreferenceFragment.this.getActivity()).clearUserLocation();
                }
                return false;
            }
        });*/
        this.update.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ProfilePreferenceFragment.this.startActivity(new Intent(ProfilePreferenceFragment.this.getActivity(), AppSetupActivity.class));
                return true;

            }
        });
    }
}
