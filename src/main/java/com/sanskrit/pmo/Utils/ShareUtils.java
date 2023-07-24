package com.sanskrit.pmo.Utils;

import static com.sanskrit.pmo.Fragments.ShareFragment.LINE_SEPARATOR_UNIX;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.sanskrit.pmo.utils.ShareObject;

public class ShareUtils {
    private  ShareObject shareObject;
    private Context context;
    public ShareUtils(Context context, ShareObject shareObject){
        this.context=context;
        this.shareObject=shareObject;



    }

    public void shareToFacebookViaApp() {
       //String shareString = shareObject.getTitle() + LINE_SEPARATOR_UNIX + shareObject.getUrl();
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (shareObject.getImageUri() == null) {
            String shareString = shareObject.getTitle() + LINE_SEPARATOR_UNIX + this.shareObject.getUrl();
            intent.putExtra("android.intent.extra.TEXT", shareString);
            intent.setType("text/plain");
        } else {
            intent.putExtra("android.intent.extra.STREAM", shareObject.getImageUri());
            intent.setType("image/*");
        }
        context.startActivity(Intent.createChooser(intent, "Share Image!"));
    }
}
