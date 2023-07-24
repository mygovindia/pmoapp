package com.sanskrit.pmo.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sanskrit.pmo.Activities.SignInActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;

import java.util.Locale;

public class LanguageSelectionFragment extends Fragment {
    TextView allLanguages;
    ImageView imageViewRadioEn, imageViewRadioHi;
    ImageButton next;

    LinearLayout linearLayoutHindi, linearLayoutEnglish;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_language_setup, container, false);
        this.imageViewRadioEn = (ImageView) rootView.findViewById(R.id.language_english);
        this.imageViewRadioHi = (ImageView) rootView.findViewById(R.id.language_hindi);

        this.allLanguages = (TextView) rootView.findViewById(R.id.show_all_languages);
        this.next = (ImageButton) rootView.findViewById(R.id.next);
        this.linearLayoutHindi = (LinearLayout) rootView.findViewById(R.id.linear_hi);
        this.linearLayoutEnglish = (LinearLayout) rootView.findViewById(R.id.linear_en);
        imageViewRadioEn.setBackgroundResource(R.drawable.ic_checked);
        imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
        linearLayoutHindi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                allLanguages.setText("Hindi");
                imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
                imageViewRadioHi.setBackgroundResource(R.drawable.ic_checked);
                if (LanguageSelectionFragment.this.getActivity() != null) {
                    PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "hi");
                }
                LanguageSelectionFragment.this.setDefaultLocale();
            }
        });

        linearLayoutEnglish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                allLanguages.setText("English");
                imageViewRadioEn.setBackgroundResource(R.drawable.ic_checked);
                imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
                if (LanguageSelectionFragment.this.getActivity() != null) {
                    PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "en");
                }
                LanguageSelectionFragment.this.setDefaultLocale();
            }


        });

        this.next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LanguageSelectionFragment.this.startActivity(new Intent(LanguageSelectionFragment.this.getActivity(), SignInActivity.class));

            }
        });
        this.allLanguages.setPaintFlags(this.allLanguages.getPaintFlags() | 8);
        this.allLanguages.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new AppLanguagesFragment().show(LanguageSelectionFragment.this.getChildFragmentManager(), "Languages Fragment");

            }
        });
        return rootView;
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

    public void onLanguageChanged(String laguageString) {

        if (laguageString.equalsIgnoreCase("english")) {
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_checked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "en");

        } else if (laguageString.equalsIgnoreCase("hindi")) {
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_checked);
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "hi");
        } else if (laguageString.equalsIgnoreCase("assamese")) {
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "asm");
        } else if (laguageString.equalsIgnoreCase("gujarati")) {
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "gu");

        } else if (laguageString.equalsIgnoreCase("kannada")) {
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "kn");

        } else if (laguageString.equalsIgnoreCase("telugu")) {
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "te");

        } else if (laguageString.equalsIgnoreCase("bengali")) {
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "bn");

        } else if (laguageString.equalsIgnoreCase("odia")) {
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "ory");
        } else if (laguageString.equalsIgnoreCase("punjabi")) {
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "pa");
        } else if (laguageString.equalsIgnoreCase("tamil")) {
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "ta");

        } else if (laguageString.equalsIgnoreCase("marathi")) {
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "mr");
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
        } else if (laguageString.equalsIgnoreCase("malayalam")) {
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "ml");
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
        } else if (laguageString.equalsIgnoreCase("manipuri")) {
            PreferencesUtility.setLanguagePreference(LanguageSelectionFragment.this.getActivity(), "mni");
            imageViewRadioEn.setBackgroundResource(R.drawable.ic_unchecked);
            imageViewRadioHi.setBackgroundResource(R.drawable.ic_unchecked);
        }
        setDefaultLocale();
        allLanguages.setText(laguageString);

    }
}
