package com.sanskrit.pmo.utils;

import android.graphics.Bitmap;
import android.graphics.Color;


import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.palette.graphics.Palette;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ColorUtils {
    public static final int IS_DARK = 1;
    public static final int IS_LIGHT = 0;
    public static final int LIGHTNESS_UNKNOWN = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Lightness {
    }

    private ColorUtils() {
    }

    @ColorInt
    @CheckResult
    public static int modifyAlpha(@ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        return (ViewCompat.MEASURED_SIZE_MASK & color) | (alpha << 24);
    }

    @ColorInt
    @CheckResult
    public static int modifyAlpha(@ColorInt int color, @FloatRange(from = 0.0d, to = 1.0d) float alpha) {
        return modifyAlpha(color, (int) (255.0f * alpha));
    }

    @ColorInt
    @CheckResult
    public static int blendColors(@ColorInt int color1, @ColorInt int color2, @FloatRange(from = 0.0d, to = 1.0d) float ratio) {
        float inverseRatio = 1.0f - ratio;
        return Color.argb((int) ((((float) Color.alpha(color1)) * inverseRatio) + (((float) Color.alpha(color2)) * ratio)), (int) ((((float) Color.red(color1)) * inverseRatio) + (((float) Color.red(color2)) * ratio)), (int) ((((float) Color.green(color1)) * inverseRatio) + (((float) Color.green(color2)) * ratio)), (int) ((((float) Color.blue(color1)) * inverseRatio) + (((float) Color.blue(color2)) * ratio)));
    }

    public static int isDark(Palette palette) {
        Palette.Swatch mostPopulous = getMostPopulousSwatch(palette);
        if (mostPopulous == null) {
            return 2;
        }
        return isDark(mostPopulous.getHsl()) ? 1 : 0;
    }

    @Nullable
    public static Palette.Swatch getMostPopulousSwatch(Palette palette) {
        Palette.Swatch mostPopulous = null;
        if (palette != null) {
            for (Palette.Swatch swatch : palette.getSwatches()) {
                if (mostPopulous == null || swatch.getPopulation() > mostPopulous.getPopulation()) {
                    mostPopulous = swatch;
                }
            }
        }
        return mostPopulous;
    }

    public static boolean isDark(@NonNull Bitmap bitmap) {
        return isDark(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
    }

    public static boolean isDark(@NonNull Bitmap bitmap, int backupPixelX, int backupPixelY) {
        Palette palette = Palette.from(bitmap).maximumColorCount(3).generate();
        if (palette == null || palette.getSwatches().size() <= 0) {
            return isDark(bitmap.getPixel(backupPixelX, backupPixelY));
        }
        if (isDark(palette) == 1) {
            return true;
        }
        return false;
    }

    public static boolean isDark(float[] hsl) {
        return hsl[2] < 0.5f;
    }

    public static boolean isDark(@ColorInt int color) {
        float[] hsl = new float[3];
        androidx.core.graphics.ColorUtils.colorToHSL(color, hsl);
        return isDark(hsl);
    }

    @ColorInt
    public static int scrimify(@ColorInt int color, boolean isDark, @FloatRange(from = 0.0d, to = 1.0d) float lightnessMultiplier) {
        float[] hsl = new float[3];
        androidx.core.graphics.ColorUtils.colorToHSL(color, hsl);
        if (isDark) {
            lightnessMultiplier = 1.0f - lightnessMultiplier;
        } else {
            lightnessMultiplier += 1.0f;
        }
        hsl[2] = com.sanskrit.pmo.utils.MathUtils.constrain(0.0f, 1.0f, hsl[2] * lightnessMultiplier);
        return androidx.core.graphics.ColorUtils.HSLToColor(hsl);
    }

    @ColorInt
    public static int scrimify(@ColorInt int color, @FloatRange(from = 0.0d, to = 1.0d) float lightnessMultiplier) {
        return scrimify(color, isDark(color), lightnessMultiplier);
    }
}
