package com.sanskrit.pmo.utils;

import android.os.Build.VERSION;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.sanskrit.pmo.R;


public class FabAnimationUtils {
    private static final long DEFAULT_DURATION = 300;
    private static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();

    public interface ScaleCallback {
        void onAnimationEnd();

        void onAnimationStart();
    }

    public static void scaleIn(View fab) {
        scaleIn(fab, DEFAULT_DURATION, null);
    }

    public static void scaleIn(final View fab, long duration, final ScaleCallback callback) {
        fab.setVisibility(View.VISIBLE);
        if (VERSION.SDK_INT >= 14) {
            ViewCompat.animate(fab).scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(duration).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(new ViewPropertyAnimatorListener() {
                public void onAnimationStart(View view) {
                    if (callback != null) {
                        callback.onAnimationStart();
                    }
                }

                public void onAnimationCancel(View view) {
                }

                public void onAnimationEnd(View view) {
                    view.setVisibility(View.VISIBLE);
                    if (callback != null) {
                        callback.onAnimationEnd();
                    }
                }
            }).start();
            return;
        }
        Animation anim = AnimationUtils.loadAnimation(fab.getContext(), R.anim.design_fab_out);
        anim.setDuration(duration);
        anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        anim.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
                if (callback != null) {
                    callback.onAnimationStart();
                }
            }

            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.VISIBLE);
                if (callback != null) {
                    callback.onAnimationEnd();
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        fab.startAnimation(anim);
    }

    public static void scaleOut(View fab) {
        scaleOut(fab, DEFAULT_DURATION, null);
    }

    public static void scaleOut(View fab, ScaleCallback callback) {
        scaleOut(fab, DEFAULT_DURATION, callback);
    }

    public static void scaleOut(View fab, long duration) {
        scaleOut(fab, duration, null);
    }

    public static void scaleOut(final View fab, long duration, final ScaleCallback callback) {
        if (VERSION.SDK_INT >= 14) {
            ViewCompat.animate(fab).scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).setDuration(duration).withLayer().setListener(new ViewPropertyAnimatorListener() {
                public void onAnimationStart(View view) {
                    if (callback != null) {
                        callback.onAnimationStart();
                    }
                }

                public void onAnimationCancel(View view) {
                }

                public void onAnimationEnd(View view) {
                    view.setVisibility(View.INVISIBLE);
                    if (callback != null) {
                        callback.onAnimationEnd();
                    }
                }
            }).start();
            return;
        }
        Animation anim = AnimationUtils.loadAnimation(fab.getContext(), R.anim.design_fab_out);
        anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        anim.setDuration(duration);
        anim.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
                if (callback != null) {
                    callback.onAnimationStart();
                }
            }

            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.INVISIBLE);
                if (callback != null) {
                    callback.onAnimationEnd();
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        fab.startAnimation(anim);
    }
}
