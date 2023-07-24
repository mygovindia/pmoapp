package com.sanskrit.pmo.Utils;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.sanskrit.pmo.Activities.VoiceOutActivity;
import com.sanskrit.pmo.Activities.VoiceoutShareActivity;
import com.sanskrit.pmo.Models.SMSContact;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.twitter.core.TwitterCore;
import com.sanskrit.pmo.utils.ShareObject;
import com.sanskrit.pmo.utils.Utils;
import com.sromku.simple.fb.SimpleFacebook;

import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static com.sanskrit.pmo.Fragments.ShareFragment.LINE_SEPARATOR_UNIX;

import androidx.fragment.app.DialogFragment;

public class VoiceOutDialog extends DialogFragment {
    private static ShareObject shareObject;
    private static String shareString;
    boolean isFacebook;
    boolean isSms;
    boolean isTwitter;
    SimpleFacebook simpleFacebook;
    String array[] = new String[3];
    boolean bools[] = new boolean[3];

    String selectedItems;


    public static VoiceOutDialog newInstance(ShareObject object, String shareString) {
        VoiceOutDialog.shareString = shareString;
        Bundle args = new Bundle();
        args.putParcelable("shareObject", Parcels.wrap(object));
        VoiceOutDialog fragment = new VoiceOutDialog();
        fragment.setArguments(args);
        shareObject = object;

        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (Utils.is15()) {
            simpleFacebook = SimpleFacebook.getInstance(getActivity());
            isFacebook = simpleFacebook.isLogin();
        } else isFacebook = false;
       // isTwitter = TwitterCore.getInstance().getSessionManager().getActiveSession() != null;

        this.isSms = false;
        array[0] = "Facebook";
        array[1] = "Twitter";
        //array[2] = "SMS Message";
        array[2] = "Whatsapp";
        bools[0] = true;
        bools[1] = true;
        bools[2] = true;
        //bools[3] = true;
        //selectedItems.add("facebook");
        // selectedItems.add(BuildConfig.ARTIFACT_ID);
        //selectedItems.add("whatsapp");

       /* try {
            boolean cacheExists = Reservoir.contains(Constants.CACHE_SMS_CONTACTS);
            if (cacheExists) {
                List<SMSContact> contacts = new ArrayList<>();
                try {
                    Type resultType = new TypeToken<List<SMSContact>>() {
                    }.getType();
                    contacts = Reservoir.get(Constants.CACHE_SMS_CONTACTS, resultType);
                    if (contacts != null && contacts.size() != 0) {
                        array[2] = "SMS message\n" + contacts.size() + " recipient(s)";
                        isSms = true;
                    } else {
                        array[2] = "SMS message\nNo contacts selected";
                        bools[2] = false;

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {
                array[2] = "SMS message\nNo contacts selected";
                bools[2] = false;

            }
        } catch (Exception e) {
            e.printStackTrace();

        }*/


        try {
//            if (!this.isFacebook) {
//                array[0] = "Facebook\nAccount not authorized. Will not be shared.";
//                bools[0] = false;
//                //selectedItems.remove("facebook");
//            }
//            if (!this.isTwitter) {
//                array[1] = "Twitter\nAccount not authorized. Will not be shared.";
//                bools[1] = false;
//                //selectedItems.remove(BuildConfig.ARTIFACT_ID);
//            }
//            if (!isWhatsappInstalled()) {
//                array[2] = "Whatsapp\nWhatsapp is not installed";
//                bools[2] = false;
//                // selectedItems.remove("whatsapp");
//            }
            /*if (!shouldUseSMS()) {
                array[2] = "SMS\nSMS not available";
                bools[2] = false;
                //selectedItems.remove("sms");
            }*/
        } catch (Exception e22) {
            e22.printStackTrace();
        }


        return new Builder(getActivity()).setTitle(R.string.voice_out).setSingleChoiceItems(array, -1, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    selectedItems = "facebook";
                } else if (which == 1) {
                    selectedItems = "twitter";
                } /*else if (which == 2) {
                    selectedItems = "sms";
                } */ else if (which == 2) {
                    selectedItems = "whatsapp";
                } else {
                    selectedItems = "";
                }

            }
        }).setPositiveButton(getString(R.string.share), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                boolean z = true;
                try {
                    if (selectedItems.isEmpty()) {
                        Toast.makeText(VoiceOutDialog.this.getActivity(), "Please select atleast one of the methods for sharing", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (selectedItems.equalsIgnoreCase("facebook") && !bools[0]) {
                        Toast.makeText(VoiceOutDialog.this.getActivity(), array[0], Toast.LENGTH_SHORT).show();

                        return;
                    } else if (selectedItems.equalsIgnoreCase("twitter") && !bools[1]) {
                        Toast.makeText(VoiceOutDialog.this.getActivity(), array[1], Toast.LENGTH_SHORT).show();
                        return;
                    }/* else if (selectedItems.equalsIgnoreCase("sms") && !bools[2]) {
                        Toast.makeText(VoiceOutDialog.this.getActivity(), array[2], Toast.LENGTH_SHORT).show();
                        return;
                    }*/ else if (selectedItems.equalsIgnoreCase("whatsapp") && !bools[2]) {
                        Toast.makeText(VoiceOutDialog.this.getActivity(), array[2], Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (selectedItems.contains("facebook")) {
                        try {
                            shareToFacebookViaApp();
                        }catch (ActivityNotFoundException a){
                            Toast.makeText(VoiceOutDialog.this.getActivity(), array[0], Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    if (selectedItems.contains("twitter")) {
                        try {
                            shareToTwitterViaApp();
                        }catch (Exception a){
                            Toast.makeText(VoiceOutDialog.this.getActivity(), array[1], Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (selectedItems.contains("whatsapp")) {
                        try {
                            shareToWhatsAppViaApp();
                        }catch (ActivityNotFoundException a){
                            Toast.makeText(VoiceOutDialog.this.getActivity(), array[2], Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Intent intent = new Intent(getActivity(), VoiceoutShareActivity.class);
                        intent.putExtra("facebook", selectedItems.contains("facebook"));
                        intent.putExtra("twitter",  selectedItems.contains("twitter"));
                        intent.putExtra("whatsapp", selectedItems.contains("whatsapp"));
                        // intent.putExtra("sms", isSms && shouldUseSMS() && selectedItems.contains("sms"));
                        intent.putExtra("shareObject", Parcels.wrap(Parcels.unwrap(getArguments().getParcelable("shareObject"))));
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton(R.string.manage_voice_out, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().startActivity(new Intent(VoiceOutDialog.this.getActivity(), VoiceOutActivity.class));

            }
        }).create();


        /*return new Builder(getActivity()).setTitle(R.string.voice_out).setMultiChoiceItems(array, bools, new OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                switch (which) {
                    case 0:
                        if (isChecked)
                        {
                            try {
                                selectedItems="facebook";
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        return;
                    case 1:
                        if (isChecked) {
                            selectedItems=BuildConfig.ARTIFACT_ID;
                            return;
                        } else {
                            selectedItems="";
                            //selectedItems.remove(BuildConfig.ARTIFACT_ID);
                            return;
                        }
                    case 2:
                        if (isChecked) {
                            selectedItems="sms";
                            return;
                        } else {
                            //selectedItems.remove("sms");
                            return;
                        }
                    case 3:
                        if (isChecked) {
                            selectedItems="whatsapp";
                            return;
                        } else {
                            //selectedItems.remove("whatsapp");
                            return;
                        }
                    default:
                        return;
                }
            }
        }).setPositiveButton(getString(R.string.share), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                boolean z = true;
                try {
                    if (selectedItems.isEmpty()) {
                        Toast.makeText(VoiceOutDialog.this.getActivity(), "Please select atleast one of the methods for sharing", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean z2;
                    Intent intent = new Intent(VoiceOutDialog.this.getActivity(), VoiceoutShareActivity.class);
                    String str = "facebook";
                    if (VoiceOutDialog.this.isFacebook && selectedItems.contains("facebook")) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    intent.putExtra(str, z2);
                    str = BuildConfig.ARTIFACT_ID;
                    if (VoiceOutDialog.this.isTwitter && selectedItems.contains(BuildConfig.ARTIFACT_ID)) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    intent.putExtra(str, z2);
                    str = "whatsapp";
                    if (VoiceOutDialog.this.isWhatsappInstalled() && selectedItems.contains("whatsapp")) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    intent.putExtra(str, z2);
                    String str2 = "sms";
                    if (!(VoiceOutDialog.this.isSms && VoiceOutDialog.this.shouldUseSMS() && selectedItems.contains("sms"))) {
                        z = false;
                    }
                    intent.putExtra(str2, z);
                    intent.putExtra("shareObject", Parcels.wrap(Parcels.unwrap(VoiceOutDialog.this.getArguments().getParcelable("shareObject"))));
                    VoiceOutDialog.this.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton(R.string.manage_voice_out, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                VoiceOutDialog.this.startActivity(new Intent(VoiceOutDialog.this.getActivity(), VoiceOutActivity.class));

            }
        }).create();*/
    }

    private void shareToTwitterViaApp() {
        try {
            String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s", new Object[]{urlEncode(shareObject.getTitle()), urlEncode(shareObject.getUrl())});
            Intent intent = new Intent("android.intent.action.SEND");
            if (shareObject.getImageUri() == null) {
                intent.putExtra("android.intent.extra.TEXT", shareString);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                intent.setType("text/plain");
            } else {
                intent.putExtra(Intent.EXTRA_STREAM, shareObject.getImageUri());
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_TEXT, VoiceOutDialog.this.shareString);

                intent.setType("image/*");
            }
            boolean twitterAppFound = false;
            for (ResolveInfo info : getActivity().getPackageManager().queryIntentActivities(intent, 0)) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                    intent.setPackage(info.activityInfo.packageName);
                    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                    twitterAppFound = true;
                    break;
                }
            }
            if (!twitterAppFound) {
                intent = new Intent("android.intent.action.VIEW", Uri.parse(tweetUrl));
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String urlEncode(String s) {
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    private boolean isWhatsappInstalled() {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private boolean shouldUseSMS() {
        if (getActivity() != null) {
            PackageManager pm = this.getActivity().getPackageManager();
            return pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        } else return false;
    }

    class Token extends TypeToken<List<SMSContact>> {
        Token() {
        }
    }
    private void shareToWhatsAppViaApp() {

        Intent intent = new Intent("android.intent.action.SEND");


        if (shareObject.getImageUri() == null) {
            String shareString = shareObject.getTitle() + LINE_SEPARATOR_UNIX + this.shareObject.getUrl();
            intent.putExtra("android.intent.extra.TEXT", shareString);
            intent.setType("text/plain");
        } else {
            intent.putExtra("android.intent.extra.STREAM", shareObject.getImageUri());
            intent.setType("image/*");
        }
        boolean facebookAppFound = false;
        for (ResolveInfo info : getActivity().getPackageManager().queryIntentActivities(intent, 0)) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.whatsapp")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

        startActivity(intent);
    }

    private void shareToFacebookViaApp() {
        Intent intent = new Intent("android.intent.action.SEND");


        if (shareObject.getImageUri() == null) {
            String shareString = shareObject.getTitle() + LINE_SEPARATOR_UNIX + this.shareObject.getUrl();
            intent.putExtra("android.intent.extra.TEXT", shareString);
            intent.setType("text/plain");
        } else {
            intent.putExtra("android.intent.extra.STREAM", shareObject.getImageUri());
            intent.setType("image/*");
        }
        boolean facebookAppFound = false;
        for (ResolveInfo info : getActivity().getPackageManager().queryIntentActivities(intent, 0)) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }
        if (!facebookAppFound) {
            intent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + this.shareObject.getUrl()));
        }
        startActivity(intent);
    }
}
