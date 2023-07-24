package com.sanskrit.pmo.Utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class FontUtil {
    private static final Map<String, Typeface> sTypefaceCache = new HashMap();

    private FontUtil() {
    }

    public static Typeface get(Context context, String font) {
        Typeface typeface;
        synchronized (sTypefaceCache) {
            if (!sTypefaceCache.containsKey(font)) {
                sTypefaceCache.put(font, Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/" + font + ".otf"));
            }
            typeface = (Typeface) sTypefaceCache.get(font);
        }
        return typeface;
    }
}
