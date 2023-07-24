package com.sanskrit.pmo.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IOUtils {
    public static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static byte[] readFile(File file) throws IOException {
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            long longlength = f.length();
            int length = (int) longlength;
            if (((long) length) != longlength) {
                throw new IOException("File size >= 5 MB");
            }
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

    public static void saveArrayToPreferences(Context context, List<String> arraylist) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arraylist.size(); i++) {
            sb.append((String) arraylist.get(i)).append(",");
        }
        editor.putString(Constants.KEY_NEWS_FILTER, sb.toString()).apply();
    }

    public static List<String> getArrayFromPref(Context context) {
        String pref = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.KEY_NEWS_FILTER, null);
        if (pref != null) {
            String[] array = pref.split(",");
            List<String> filters = new ArrayList();
            if (Collections.addAll(filters, array)) {
                return filters;
            }
        }
        return new ArrayList();
    }
}
