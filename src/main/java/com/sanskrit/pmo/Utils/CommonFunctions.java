package com.sanskrit.pmo.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.Html;
import android.text.Spanned;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.sanskrit.pmo.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.OkHttpClient;

public class CommonFunctions {
    private static final int CAPTURE_IMAGE = 2;
    public static final String NetworkMessage = "Please Check Network Connection!";
    private static final int PICK_IMAGE = 1;
    private static final int PIC_File = 3;
    public static final String ServerMessage = "Unable to Connect";
    public static Builder cbuilder1 = null;
    private static final int connectiontimeout = 30;
    public static Uri mImageCaptureUri;



    final class C07142 implements OnClickListener {
        final /* synthetic */ Activity val$activity;

        C07142(Activity activity) {
            this.val$activity = activity;
        }

        public final void onClick(DialogInterface dialogInterface, int i) {
            Intent intent;
            if (i == 0) {
                try {
                    intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra("output", CommonFunctions.setImageUri(this.val$activity));
                    this.val$activity.startActivityForResult(intent, 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (i == 1) {
                try {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    this.val$activity.startActivityForResult(Intent.createChooser(intent, ""), 1);
                } catch (Exception e2) {
                }
            } else if (i == 2) {
                try {
                    intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.addCategory("android.intent.category.OPENABLE");
                    intent.setType("application/pdf");
                    this.val$activity.startActivityForResult(Intent.createChooser(intent, "File"), 3);
                } catch (Exception e3) {
                }
            }
        }
    }

    final class C07153 implements OnCancelListener {
        C07153() {
        }

        public final void onCancel(DialogInterface dialogInterface) {
            dialogInterface.dismiss();
        }
    }

    final class C07164 implements OnClickListener {
        final /* synthetic */ Activity val$activity;

        C07164(Activity activity) {
            this.val$activity = activity;
        }

        public final void onClick(DialogInterface dialogInterface, int i) {
            Intent intent;
            if (i == 0) {
                intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra("output", CommonFunctions.setImageUri(this.val$activity));
                this.val$activity.startActivityForResult(intent, 2);
            } else if (i == 1) {
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                this.val$activity.startActivityForResult(Intent.createChooser(intent, ""), 1);
            }
        }
    }



    public static void ShowMessageToUser(Context context, String str) {
        if (cbuilder1 == null) {
            Builder builder = new Builder(context);
            cbuilder1 = builder;
            builder.setTitle(context.getResources().getString(R.string.message));
            cbuilder1.setCancelable(false);
            cbuilder1.setMessage(str);
            cbuilder1.setPositiveButton(context.getResources().getString(R.string.ok), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CommonFunctions.cbuilder1 = null;
                    dialog.dismiss();
                }
            });
            try {
                cbuilder1.show();
            } catch (Exception e) {
            }
        }
    }

    public static Dialog showDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.custom_progress_dialog);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        return dialog;
    }

    public static Spanned fromHtml(String str) {
        if (VERSION.SDK_INT >= 24) {
            return Html.fromHtml(str, 0);
        }
        return Html.fromHtml(str);
    }

    public static String Datecheck1(String str) {
        String str2 = "";
        try {
            str2 = new SimpleDateFormat("MMM dd,yyyy hh:mm:ss aa", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(str));
        } catch (ParseException e) {
        }
        return str2;
    }

    public static String extractUrls(String str) {
        String str2 = "";
        Matcher matcher = Pattern.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", 2).matcher(str);
        while (matcher.find()) {
            str2 = str.substring(matcher.start(0), matcher.end(0));
            if (str2.contains("https://www.youtube.com/embed/")) {
                break;
            }
        }
        return str2;
    }



    private static Uri setImageUri(Context context) {
        File file = new File(Environment.getExternalStorageDirectory(), new Date().getTime() + "image.png");
        mImageCaptureUri = Uri.fromFile(file);
        file.getAbsolutePath();
        return mImageCaptureUri;
    }

    public static boolean isNetworkOnline(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isConnected();
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static String filesize(String str, long j) {
        long folderSize = getFolderSize(new File(str));
        if ((folderSize / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID > 8) {
            return "";
        }
        if (((j + folderSize) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID < 10) {
            return String.valueOf(folderSize);
        }
        return "0";
    }

    private static long getFolderSize(File file) {
        if (!file.isDirectory()) {
            return file.length();
        }
        File[] listFiles = file.listFiles();
        long j = 0;
        int i = 0;
        while (i < listFiles.length) {
            long folderSize = getFolderSize(listFiles[i]) + j;
            i++;
            j = folderSize;
        }
        return j;
    }

    public static String stripNonDigits(CharSequence charSequence) {
        StringBuilder stringBuilder = new StringBuilder(charSequence.length());
        for (int i = 0; i < charSequence.length(); i++) {
            char charAt = charSequence.charAt(i);
            if (charAt > '/' && charAt < ':') {
                stringBuilder.append(charAt);
            }
        }
        return stringBuilder.toString();
    }

    public static OkHttpClient HtppcallforGet() {
        return new OkHttpClient.Builder().connectTimeout(30, TimeUnit.MINUTES).writeTimeout(30, TimeUnit.MINUTES).readTimeout(30, TimeUnit.MINUTES).build();
    }

    public static OkHttpClient HtppcallforGetLogin() {
        return new OkHttpClient.Builder().addInterceptor(new LogginInterceptor()).connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
    }
    public static OkHttpClient HtppcallforInterprator() {
        return new OkHttpClient.Builder().addInterceptor(new GetInterceptor()).connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
    }


}
