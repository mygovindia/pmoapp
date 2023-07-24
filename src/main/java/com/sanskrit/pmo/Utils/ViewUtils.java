package com.sanskrit.pmo.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;

import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Property;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;


public class ViewUtils {
    public static final Property<View, Integer> BACKGROUND_COLOR = new com.sanskrit.pmo.utils.AnimUtils.IntProperty<View>("backgroundColor") {
        public void setValue(View view, int value) {
            view.setBackgroundColor(value);
        }

        public Integer get(View view) {
            Drawable d = view.getBackground();
            if (d instanceof ColorDrawable) {
                return Integer.valueOf(((ColorDrawable) d).getColor());
            }
            return Integer.valueOf(0);
        }
    };
    public static final ViewOutlineProvider CIRCULAR_OUTLINE = new C13513();
    public static final Property<ImageView, Integer> IMAGE_ALPHA = new com.sanskrit.pmo.utils.AnimUtils.IntProperty<ImageView>("imageAlpha") {
        public void setValue(ImageView imageView, int value) {
            imageView.setImageAlpha(value);
        }

        public Integer get(ImageView imageView) {
            return Integer.valueOf(imageView.getImageAlpha());
        }
    };
    private static int actionBarSize = -1;

    static class C13513 extends ViewOutlineProvider {
        C13513() {
        }

        public void getOutline(View view, Outline outline) {
            outline.setOval(view.getPaddingLeft(), view.getPaddingTop(), view.getWidth() - view.getPaddingRight(), view.getHeight() - view.getPaddingBottom());
        }
    }

    private ViewUtils() {
    }

    public static int getActionBarSize(Context context) {
        if (actionBarSize < 0) {
            TypedValue value = new TypedValue();
            context.getTheme().resolveAttribute(16843499, value, true);
            actionBarSize = TypedValue.complexToDimensionPixelSize(value.data, context.getResources().getDisplayMetrics());
        }
        return actionBarSize;
    }

    public static boolean isNavBarOnBottom(@NonNull Context context) {
        boolean canMove;
        Resources res = context.getResources();
        Configuration cfg = context.getResources().getConfiguration();
        DisplayMetrics dm = res.getDisplayMetrics();
        if (dm.widthPixels == dm.heightPixels || cfg.smallestScreenWidthDp >= 600) {
            canMove = false;
        } else {
            canMove = true;
        }
        if (!canMove || dm.widthPixels < dm.heightPixels) {
            return true;
        }
        return false;
    }

    public static RippleDrawable createRipple(@ColorInt int color, @FloatRange(from = 0.0d, to = 1.0d) float alpha, boolean bounded) {
        return new RippleDrawable(ColorStateList.valueOf(com.sanskrit.pmo.utils.ColorUtils.modifyAlpha(color, alpha)), null, bounded ? new ColorDrawable(-1) : null);
    }

    public static RippleDrawable createRipple(@NonNull Palette palette, @FloatRange(from = 0.0d, to = 1.0d) float darkAlpha, @FloatRange(from = 0.0d, to = 1.0d) float lightAlpha, @ColorInt int fallbackColor, boolean bounded) {
        Drawable colorDrawable;
        int rippleColor = fallbackColor;
        if (palette != null) {
            if (palette.getVibrantSwatch() != null) {
                rippleColor = com.sanskrit.pmo.utils.ColorUtils.modifyAlpha(palette.getVibrantSwatch().getRgb(), darkAlpha);
            } else if (palette.getLightVibrantSwatch() != null) {
                rippleColor = com.sanskrit.pmo.utils.ColorUtils.modifyAlpha(palette.getLightVibrantSwatch().getRgb(), lightAlpha);
            } else if (palette.getDarkVibrantSwatch() != null) {
                rippleColor = com.sanskrit.pmo.utils.ColorUtils.modifyAlpha(palette.getDarkVibrantSwatch().getRgb(), darkAlpha);
            } else if (palette.getMutedSwatch() != null) {
                rippleColor = com.sanskrit.pmo.utils.ColorUtils.modifyAlpha(palette.getMutedSwatch().getRgb(), darkAlpha);
            } else if (palette.getLightMutedSwatch() != null) {
                rippleColor = com.sanskrit.pmo.utils.ColorUtils.modifyAlpha(palette.getLightMutedSwatch().getRgb(), lightAlpha);
            } else if (palette.getDarkMutedSwatch() != null) {
                rippleColor = com.sanskrit.pmo.utils.ColorUtils.modifyAlpha(palette.getDarkMutedSwatch().getRgb(), darkAlpha);
            }
        }
        ColorStateList valueOf = ColorStateList.valueOf(rippleColor);
        if (bounded) {
            colorDrawable = new ColorDrawable(-1);
        } else {
            colorDrawable = null;
        }
        return new RippleDrawable(valueOf, null, colorDrawable);
    }

    public static void setLightStatusBar(@NonNull View view) {
        if (VERSION.SDK_INT >= 23) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() | 8192);
        }
    }

    public static void clearLightStatusBar(@NonNull View view) {
        if (VERSION.SDK_INT >= 23) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() & -8193);
        }
    }

    public static float getSingleLineTextSize(String text, TextPaint paint, float targetWidth, float low, float high, float precision, DisplayMetrics metrics) {
        float mid = (low + high) / 2.0f;
        paint.setTextSize(TypedValue.applyDimension(0, mid, metrics));
        float maxLineWidth = paint.measureText(text);
        if (high - low < precision) {
            return low;
        }
        if (maxLineWidth > targetWidth) {
            return getSingleLineTextSize(text, paint, targetWidth, low, mid, precision, metrics);
        }
        return maxLineWidth < targetWidth ? getSingleLineTextSize(text, paint, targetWidth, mid, high, precision, metrics) : mid;
    }

    public static boolean viewsIntersect(View view1, View view2) {
        if (view1 == null || view2 == null) {
            return false;
        }
        int[] view1Loc = new int[2];
        view1.getLocationOnScreen(view1Loc);
        Rect view1Rect = new Rect(view1Loc[0], view1Loc[1], view1Loc[0] + view1.getWidth(), view1Loc[1] + view1.getHeight());
        int[] view2Loc = new int[2];
        view2.getLocationOnScreen(view2Loc);
        return view1Rect.intersect(new Rect(view2Loc[0], view2Loc[1], view2Loc[0] + view2.getWidth(), view2Loc[1] + view2.getHeight()));
    }

    public static void setPaddingStart(View view, int paddingStart) {
        view.setPaddingRelative(paddingStart, view.getPaddingTop(), view.getPaddingEnd(), view.getPaddingBottom());
    }

    public static void setPaddingTop(View view, int paddingTop) {
        view.setPaddingRelative(view.getPaddingStart(), paddingTop, view.getPaddingEnd(), view.getPaddingBottom());
    }

    public static void setPaddingEnd(View view, int paddingEnd) {
        view.setPaddingRelative(view.getPaddingStart(), view.getPaddingTop(), paddingEnd, view.getPaddingBottom());
    }

    public static void setPaddingBottom(View view, int paddingBottom) {
        view.setPaddingRelative(view.getPaddingStart(), view.getPaddingTop(), view.getPaddingEnd(), paddingBottom);
    }
}
