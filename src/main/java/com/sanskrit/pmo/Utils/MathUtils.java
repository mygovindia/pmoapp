package com.sanskrit.pmo.utils;

public class MathUtils {
    private MathUtils() {
    }

    public static float constrain(float min, float max, float v) {
        return Math.max(min, Math.min(max, v));
    }
}
