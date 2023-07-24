package com.sanskrit.pmo.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;

import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.sanskrit.pmo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.annotation.NonNull;

public class Utils {
    public static final boolean isOnline(Context context) {
        if (context == null) {
            return true;
        }
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("Share",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("Share", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("Share", "Error accessing file: " + e.getMessage());
        }

        return Uri.fromFile(pictureFile);
    }

    private static File getOutputMediaFile() {
        try {
            File mediaStorageDir = new File(getApplicationContext().getPackageName()
                    + "/Android/data/"
                    + getApplicationContext().getPackageName()
                    + "/Files");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
            File mediaFile;
            String mImageName = "tempshare.jpg";
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
            if (mediaFile.exists()) {
                mediaFile.delete();
            }
            return mediaFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final String makeShortTimeString(Context context, long secs) {
        long hours = secs / 3600;
        secs %= 3600;
        long mins = secs / 60;
        secs %= 60;
        return String.format(context.getResources().getString(hours == 0 ? R.string.durationformatshort : R.string.durationformatlong), new Object[]{Long.valueOf(hours), Long.valueOf(mins), Long.valueOf(secs)});
    }

    public static boolean isMarshmallow() {
        return VERSION.SDK_INT >= 23;
    }

    public static boolean isLollipop() {
        return VERSION.SDK_INT >= 21;
    }

    public static boolean isKitkat() {
        return VERSION.SDK_INT >= 19;
    }

    public static boolean isJellyBeanMR2() {
        return VERSION.SDK_INT >= 18;
    }

    public static boolean isJellyBeanMR1() {
        return VERSION.SDK_INT >= 17;
    }

    public static boolean isGingerbread() {
        return VERSION.SDK_INT >= 9;
    }

    public static boolean isICS() {
        return VERSION.SDK_INT >= 14;
    }

    public static boolean is15() {
        return VERSION.SDK_INT >= 15;
    }

    public static boolean is11() {
        return VERSION.SDK_INT >= 11;
    }

    public static String getPermaToken(Context context) {
        String permaToken = String.valueOf(Build.BOARD.length() % 10) + String.valueOf(Build.BRAND.length() % 10) + String.valueOf(Build.CPU_ABI.length() % 10) + String.valueOf(Build.DEVICE.length() % 10) + String.valueOf(Build.DISPLAY.length() % 10) + String.valueOf(Build.HOST.length() % 10) + String.valueOf(Build.ID.length() % 10) + String.valueOf(Build.MANUFACTURER.length() % 10) + String.valueOf(Build.MODEL.length() % 10) + String.valueOf(Build.PRODUCT.length() % 10) + String.valueOf(Build.TAGS.length() % 10) + String.valueOf(Build.TYPE.length() % 10) + String.valueOf(Build.USER.length() % 10);
        long unixTime = System.currentTimeMillis() / 1000;
        if (Secure.getString(context.getContentResolver(), "android_id") != null) {
            permaToken = permaToken + "$" + Secure.getString(context.getContentResolver(), "android_id");
        } else {
            permaToken = permaToken + "$000000";
        }
        return permaToken + "#" + String.valueOf(unixTime);
    }

    public static String decodeString(String string) {
        try {
            return new String(Base64.decode(string, 0), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeString(String string) {
        try {
            return Base64.encodeToString(string.getBytes("UTF-8"), 0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String urlEncodeString(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static void setBackground(View view, Drawable d) {
        if (VERSION.SDK_INT >= 16) {
            view.setBackground(d);
        } else {
            view.setBackgroundDrawable(d);
        }
    }

    public static void postInvalidateOnAnimation(View view) {
        if (VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation();
        } else {
            view.invalidate();
        }
    }

   /* private Point getLocationInView(View src, View target) {
        int[] l0 = new int[2];
        src.getLocationOnScreen(l0);
        l1 = new int[2];
        target.getLocationOnScreen(l1);
        l1[0] = (l1[0] - l0[0]) + (target.getWidth() / 2);
        l1[1] = (l1[1] - l0[1]) + (target.getHeight() / 2);
        return new Point(l1[0], l1[1]);
    }*/

    public static String getIpAddress(Context context) {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return Formatter.formatIpAddress(inetAddress.hashCode());
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return "0.0.0.0";
    }

    public static String generatePmIndiaShareUrl(String id) {
        return "http://pmindia.gov.in/?p=" + id;
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static String getStringByLocale(Activity context, int id, String locale) {
        Log.d("Utils", "getStringByLocale: " + locale);
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));
        return context.createConfigurationContext(configuration).getResources().getString(id);
    }
}
