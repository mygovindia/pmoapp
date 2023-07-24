package com.sanskrit.pmo.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.utils.AnimUtils;
import com.sanskrit.pmo.utils.FabAnimationUtils;
import com.sanskrit.pmo.utils.TintHelper;
import com.sanskrit.pmo.utils.Utils;
import com.sanskrit.pmo.utils.ViewUtils;



public class SocialFeedFragment extends Fragment {
    ViewGroup confirmSaveContainer;
    FloatingActionButton fab;
    RadioButton facebook;
    RadioGroup radioGroup;
    View resultsScrim;
    Button saveConfirmed;
    RadioButton twitter;
    RadioButton youtube;


    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        } catch (Exception e) {
            //Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_social_feed, container, false);
        this.fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        this.confirmSaveContainer = (ViewGroup) rootView.findViewById(R.id.confirm_save_container);
        this.radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        this.resultsScrim = rootView.findViewById(R.id.results_scrim);
        this.saveConfirmed = (Button) rootView.findViewById(R.id.save_confirmed);
        this.twitter = (RadioButton) rootView.findViewById(R.id.radio_twitter);
        this.youtube = (RadioButton) rootView.findViewById(R.id.radio_youtube);
        this.facebook = (RadioButton) rootView.findViewById(R.id.radio_facebook);
        twitter.setChecked(true);
        //  getChildFragmentManager().beginTransaction().replace(R.id.container, new TweetsFragment()).commit();


        String socialFeedPreferncee = PreferencesUtility.getSocialFeedPreferncee(getActivity());
        fab.setImageResource(R.drawable.twitter_filter);

        if (socialFeedPreferncee.equals("youtube")) {
            twitter.setChecked(false);
            facebook.setChecked(false);
            youtube.setChecked(true);
            fab.setImageResource(R.drawable.youtube_filter);
            getChildFragmentManager().beginTransaction().replace(R.id.container, new YoutubeFeedFragment()).addToBackStack(null).commit();
            if (PreferencesUtility.getTheme(getActivity()).equals("black")) {
                this.fab.setImageDrawable(TintHelper.tintDrawable(this.fab.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));

            }

        } else if (socialFeedPreferncee.equals("twitter")) {
            twitter.setChecked(true);
            facebook.setChecked(false);
            youtube.setChecked(false);
            fab.setImageResource(R.drawable.twitter_filter);

            getChildFragmentManager().beginTransaction().replace(R.id.container, new TweetsFragment()).addToBackStack(null).commit();
            if (PreferencesUtility.getTheme(getActivity()).equals("black")) {
                this.fab.setImageDrawable(TintHelper.tintDrawable(this.fab.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));

            }

        } else if (socialFeedPreferncee.equals("facebook")) {
            twitter.setChecked(false);
            facebook.setChecked(true);
            youtube.setChecked(false);
            fab.setImageResource(R.drawable.facebook_filter);

            getChildFragmentManager().beginTransaction().replace(R.id.container, new FacebookFeedFragment()).addToBackStack(null).commit();
            if (PreferencesUtility.getTheme(getActivity()).equals("black")) {
                this.fab.setImageDrawable(TintHelper.tintDrawable(this.fab.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));

            }

        }

        this.fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SocialFeedFragment.this.show();
            }
        });
        this.resultsScrim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SocialFeedFragment.this.hide();
            }
        });

        this.saveConfirmed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SocialFeedFragment.this.saveAndhide();
            }
        });

        if (PreferencesUtility.getTheme(getActivity()).equals("black")) {
            this.fab.setImageDrawable(TintHelper.tintDrawable(this.fab.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));

        }

        return rootView;
    }

    public void transactFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
    }


    private void show() {

        resultsScrim.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (Utils.isLollipop()) {
                    SocialFeedFragment.this.confirmSaveContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                    Animator reveal = ViewAnimationUtils.createCircularReveal(SocialFeedFragment.this.confirmSaveContainer, SocialFeedFragment.this.confirmSaveContainer.getWidth() / 2, SocialFeedFragment.this.confirmSaveContainer.getHeight() / 2, (float) (SocialFeedFragment.this.fab.getWidth() / 2), (float) (SocialFeedFragment.this.confirmSaveContainer.getWidth() / 2));
                    reveal.setDuration(250);
                    reveal.setInterpolator(AnimUtils.getFastOutSlowInInterpolator(SocialFeedFragment.this.getActivity()));
                    reveal.start();
                    int centerX = (SocialFeedFragment.this.fab.getLeft() + SocialFeedFragment.this.fab.getRight()) / 2;
                    int centerY = (SocialFeedFragment.this.fab.getTop() + SocialFeedFragment.this.fab.getBottom()) / 2;
                    Animator revealScrim = ViewAnimationUtils.createCircularReveal(SocialFeedFragment.this.resultsScrim, centerX, centerY, 0.0f, (float) Math.hypot((double) centerX, (double) centerY));
                    revealScrim.setDuration(400);
                    revealScrim.setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(SocialFeedFragment.this.getActivity()));
                    revealScrim.start();
                    ObjectAnimator fadeInScrim = ObjectAnimator.ofArgb(SocialFeedFragment.this.resultsScrim, ViewUtils.BACKGROUND_COLOR, new int[]{0, ContextCompat.getColor(SocialFeedFragment.this.getActivity(), R.color.scrim)});
                    fadeInScrim.setDuration(800);
                    fadeInScrim.setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(SocialFeedFragment.this.getActivity()));
                    fadeInScrim.start();
                }
                return false;
            }
        });

        String socialFeedPreferncee = PreferencesUtility.getSocialFeedPreferncee(getActivity());
        if (socialFeedPreferncee.equals("youtube")) {
            twitter.setChecked(false);
            facebook.setChecked(false);
            youtube.setChecked(true);

        } else if (socialFeedPreferncee.equals("twitter")) {
            twitter.setChecked(true);
            facebook.setChecked(false);
            youtube.setChecked(false);

        } else if (socialFeedPreferncee.equals("facebook")) {
            twitter.setChecked(false);
            facebook.setChecked(true);
            youtube.setChecked(false);

        }

        this.confirmSaveContainer.setVisibility(View.VISIBLE);
        this.resultsScrim.setVisibility(View.VISIBLE);
        this.fab.setVisibility(View.GONE);
        FabAnimationUtils.scaleOut(this.fab);

    }

    private void hide() {
        if (this.confirmSaveContainer.getVisibility() == View.VISIBLE) {
            hideFilterContainer();
        }
    }

    private void saveAndhide() {

        hide();

        if (this.confirmSaveContainer.getVisibility() == View.VISIBLE) {
            String preference;
            switch (this.radioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_twitter:
                    preference = "twitter";
                    break;
                case R.id.radio_facebook:
                    preference = "facebook";
                    break;
                case R.id.radio_youtube:
                    preference = "youtube";
                    break;
                default:
                    preference = "twitter";
                    break;
            }
            PreferencesUtility.setSocialFeedPreferncee(getActivity(), preference);
            hideFilterContainer();
            if (preference.equals("youtube")) {
                if (!(getChildFragmentManager().findFragmentById(R.id.container) instanceof YoutubeFeedFragment)) {
                    fab.setImageResource(R.drawable.youtube_filter);

                    getChildFragmentManager().beginTransaction().replace(R.id.container, new YoutubeFeedFragment()).addToBackStack(null).commit();
                    if (PreferencesUtility.getTheme(getActivity()).equals("black")) {
                        this.fab.setImageDrawable(TintHelper.tintDrawable(this.fab.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));

                    }

                    return;
                }
            } else if (preference.equals("twitter")) {
                if (!(getChildFragmentManager().findFragmentById(R.id.container) instanceof TweetsFragment)) {
                    fab.setImageResource(R.drawable.twitter_filter);

                    getChildFragmentManager().beginTransaction().replace(R.id.container, new TweetsFragment()).addToBackStack(null).commit();
                    if (PreferencesUtility.getTheme(getActivity()).equals("black")) {
                        this.fab.setImageDrawable(TintHelper.tintDrawable(this.fab.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));

                    }

                    return;
                }
            } else if (preference.equals("facebook")) {
                if (!(getChildFragmentManager().findFragmentById(R.id.container) instanceof FacebookFeedFragment)) {
                    fab.setImageResource(R.drawable.facebook_filter);

                    getChildFragmentManager().beginTransaction().replace(R.id.container, new FacebookFeedFragment()).addToBackStack(null).commit();
                    if (PreferencesUtility.getTheme(getActivity()).equals("black")) {
                        this.fab.setImageDrawable(TintHelper.tintDrawable(this.fab.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));

                    }

                    return;
                }
            }


        }
    }

    private void hideFilterContainer() {
        if (Utils.isLollipop()) {
            AnimatorSet hideConfirmation = new AnimatorSet();
            Animator[] animatorArr = new Animator[2];
            animatorArr[0] = ViewAnimationUtils.createCircularReveal(this.confirmSaveContainer, this.confirmSaveContainer.getWidth() / 2, this.confirmSaveContainer.getHeight() / 2, (float) (this.confirmSaveContainer.getWidth() / 2), (float) (this.fab.getWidth() / 2));
            animatorArr[1] = ObjectAnimator.ofArgb(this.resultsScrim, ViewUtils.BACKGROUND_COLOR, new int[]{0});
            hideConfirmation.playTogether(animatorArr);
            hideConfirmation.setDuration(150);
            hideConfirmation.setInterpolator(AnimUtils.getFastOutSlowInInterpolator(getActivity()));
            hideConfirmation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    SocialFeedFragment.this.confirmSaveContainer.setVisibility(View.GONE);
                    SocialFeedFragment.this.resultsScrim.setVisibility(View.INVISIBLE);
                    SocialFeedFragment.this.fab.setVisibility(View.VISIBLE);
                    FabAnimationUtils.scaleIn(SocialFeedFragment.this.fab);
                    super.onAnimationEnd(animation);

                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }

                @Override
                public void onAnimationPause(Animator animation) {
                    super.onAnimationPause(animation);
                }

                @Override
                public void onAnimationResume(Animator animation) {
                    super.onAnimationResume(animation);
                }
            });
            hideConfirmation.start();


            return;
        }


        this.confirmSaveContainer.setVisibility(View.GONE);
        this.resultsScrim.setVisibility(View.INVISIBLE);
        this.fab.setVisibility(View.VISIBLE);
        FabAnimationUtils.scaleIn(this.fab);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Fragment fragment = getChildFragmentManager().findFragmentById(R.id.container);
            if (fragment == null) {
                return;
            }
            if ((fragment instanceof TweetsFragment) || (fragment instanceof com.sanskrit.pmo.fragments.AuthenticatedTweetsFragment)) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
