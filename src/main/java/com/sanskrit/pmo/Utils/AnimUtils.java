package com.sanskrit.pmo.utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.os.Build;
import android.transition.Transition;
import android.transition.Transition.TransitionListener;
import android.util.ArrayMap;
import android.util.Property;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import java.util.ArrayList;

public class AnimUtils {
    private static Interpolator fastOutLinearIn;
    private static Interpolator fastOutSlowIn;
    private static Interpolator linearOutSlowIn;

    public static abstract class FloatProperty<T> extends Property<T, Float> {
        public abstract void setValue(T t, float f);

        public FloatProperty(String name) {
            super(Float.class, name);
        }

        public final void set(T object, Float value) {
            setValue(object, value.floatValue());
        }
    }


    public static <T> Property<T, Integer> createIntProperty(final IntProp<T> impl) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new IntProperty<T>(impl.name) {
                @Override
                public Integer get(T object) {
                    return impl.get(object);
                }

                @Override
                public void setValue(T object, int value) {
                    impl.set(object, value);
                }
            };
        } else {
            return new Property<T, Integer>(Integer.class, impl.name) {
                @Override
                public Integer get(T object) {
                    return impl.get(object);
                }

                @Override
                public void set(T object, Integer value) {
                    impl.set(object, value);
                }
            };
        }
    }

    /**
     * A delegate for creating a {@link Property} of <code>int</code> type.
     */
    public static abstract class IntProp<T> {

        public final String name;

        public IntProp(String name) {
            this.name = name;
        }

        public abstract void set(T object, int value);

        public abstract int get(T object);
    }

    static class AnimatorListenerWrapper implements AnimatorListener {
        private final Animator mAnimator;
        private final AnimatorListener mListener;

        public AnimatorListenerWrapper(Animator animator, AnimatorListener listener) {
            this.mAnimator = animator;
            this.mListener = listener;
        }

        public void onAnimationStart(Animator animator) {
            this.mListener.onAnimationStart(this.mAnimator);
        }

        public void onAnimationEnd(Animator animator) {
            this.mListener.onAnimationEnd(this.mAnimator);
        }

        public void onAnimationCancel(Animator animator) {
            this.mListener.onAnimationCancel(this.mAnimator);
        }

        public void onAnimationRepeat(Animator animator) {
            this.mListener.onAnimationRepeat(this.mAnimator);
        }
    }

    public static abstract class IntProperty<T> extends Property<T, Integer> {
        public abstract void setValue(T t, int i);

        public IntProperty(String name) {
            super(Integer.class, name);
        }

        public final void set(T object, Integer value) {
            setValue(object, value.intValue());
        }
    }

    public static class NoPauseAnimator extends Animator {
        private final Animator mAnimator;
        private final ArrayMap<AnimatorListener, AnimatorListener> mListeners = new ArrayMap();

        public NoPauseAnimator(Animator animator) {
            this.mAnimator = animator;
        }

        public void addListener(AnimatorListener listener) {
            AnimatorListener wrapper = new AnimatorListenerWrapper(this, listener);
            if (!this.mListeners.containsKey(listener)) {
                this.mListeners.put(listener, wrapper);
                this.mAnimator.addListener(wrapper);
            }
        }

        public void cancel() {
            this.mAnimator.cancel();
        }

        public void end() {
            this.mAnimator.end();
        }

        public long getDuration() {
            return this.mAnimator.getDuration();
        }

        public TimeInterpolator getInterpolator() {
            return this.mAnimator.getInterpolator();
        }

        public void setInterpolator(TimeInterpolator timeInterpolator) {
            this.mAnimator.setInterpolator(timeInterpolator);
        }

        public ArrayList<AnimatorListener> getListeners() {
            return new ArrayList(this.mListeners.keySet());
        }

        public long getStartDelay() {
            return this.mAnimator.getStartDelay();
        }

        public void setStartDelay(long delayMS) {
            this.mAnimator.setStartDelay(delayMS);
        }

        public boolean isPaused() {
            return this.mAnimator.isPaused();
        }

        public boolean isRunning() {
            return this.mAnimator.isRunning();
        }

        public boolean isStarted() {
            return this.mAnimator.isStarted();
        }

        public void removeAllListeners() {
            this.mListeners.clear();
            this.mAnimator.removeAllListeners();
        }

        public void removeListener(AnimatorListener listener) {
            AnimatorListener wrapper = (AnimatorListener) this.mListeners.get(listener);
            if (wrapper != null) {
                this.mListeners.remove(listener);
                this.mAnimator.removeListener(wrapper);
            }
        }

        public Animator setDuration(long durationMS) {
            this.mAnimator.setDuration(durationMS);
            return this;
        }

        public void setTarget(Object target) {
            this.mAnimator.setTarget(target);
        }

        public void setupEndValues() {
            this.mAnimator.setupEndValues();
        }

        public void setupStartValues() {
            this.mAnimator.setupStartValues();
        }

        public void start() {
            this.mAnimator.start();
        }
    }

    public static class TransitionListenerAdapter implements TransitionListener {
        public void onTransitionStart(Transition transition) {
        }

        public void onTransitionEnd(Transition transition) {
        }

        public void onTransitionCancel(Transition transition) {
        }

        public void onTransitionPause(Transition transition) {
        }

        public void onTransitionResume(Transition transition) {
        }
    }

    private AnimUtils() {
    }

    public static Interpolator getFastOutSlowInInterpolator(Context context) {
        if (fastOutSlowIn == null) {
            fastOutSlowIn = AnimationUtils.loadInterpolator(context, 17563661);
        }
        return fastOutSlowIn;
    }

    public static Interpolator getFastOutLinearInInterpolator(Context context) {
        if (fastOutLinearIn == null) {
            fastOutLinearIn = AnimationUtils.loadInterpolator(context, 17563663);
        }
        return fastOutLinearIn;
    }

    public static Interpolator getLinearOutSlowInInterpolator(Context context) {
        if (linearOutSlowIn == null) {
            linearOutSlowIn = AnimationUtils.loadInterpolator(context, 17563662);
        }
        return linearOutSlowIn;
    }

    public static float lerp(float a, float b, float t) {
        return ((b - a) * t) + a;
    }
}
