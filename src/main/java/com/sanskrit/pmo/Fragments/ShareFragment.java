package com.sanskrit.pmo.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Utils.VoiceOutDialog;
import com.sanskrit.pmo.utils.ShareObject;
import com.sanskrit.pmo.utils.Utils;
import com.sromku.simple.fb.SimpleFacebook;

import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ShareFragment extends BottomSheetDialogFragment {
    boolean alsoToSms = false;
    boolean alsoToWhatsapp = false;
    boolean alsoTofacebook = false;
    ImageView facebook;
    ImageView googleplus;
    private BottomSheetBehavior mBehavior;
    TextView more;
    ShareObject object;
    String shareString;
    TextView shareText;
    SimpleFacebook simpleFacebook;
    ImageView twitter;
    Button voiceOut;
    ImageView whatsapp;
    public static final String LINE_SEPARATOR_UNIX = "\n";

    public static ShareFragment newInstance(ShareObject shareObject) {
        ShareFragment fragment = new ShareFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("share", Parcels.wrap(shareObject));
        fragment.setArguments(bundle);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View rootView = View.inflate(getContext(), R.layout.fragment_dialog_share, null);
        dialog.setContentView(rootView);
        this.mBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        this.object = (ShareObject) Parcels.unwrap(getArguments().getParcelable("share"));
        this.shareString = this.object.getTitle() + LINE_SEPARATOR_UNIX + this.object.getUrl();
        this.twitter = (ImageView) rootView.findViewById(R.id.twitter);
        this.facebook = (ImageView) rootView.findViewById(R.id.facebook);
        this.googleplus = (ImageView) rootView.findViewById(R.id.googleplus);
        this.whatsapp = (ImageView) rootView.findViewById(R.id.whatsapp);
        this.shareText = (TextView) rootView.findViewById(R.id.share_text);
        this.more = (TextView) rootView.findViewById(R.id.more);
        if (Utils.is15()) {
            this.simpleFacebook = SimpleFacebook.getInstance(getActivity());
        }
        this.twitter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareFragment.this.shareToTwitterViaApp();
            }
        });
        this.facebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareFragment.this.shareToFacebookViaApp();

            }
        });
        this.googleplus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean packageFound = false;
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    if (ShareFragment.this.object.getImageUri() == null) {
                        intent.putExtra("android.intent.extra.TEXT", ShareFragment.this.shareString);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        intent.setType("text/plain");
                    } else {
                        intent.putExtra("android.intent.extra.STREAM", ShareFragment.this.object.getImageUri());
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(Intent.EXTRA_TEXT, ShareFragment.this.shareString);

                        intent.setType("image/*");
                    }
                    for (ResolveInfo info : ShareFragment.this.getActivity().getPackageManager().queryIntentActivities(intent, 0)) {
                        if (info.activityInfo.packageName.toLowerCase().startsWith("com.google.android.apps.plus")) {
                            intent.setPackage(info.activityInfo.packageName);
                            ShareFragment.this.startActivity(intent);
                            packageFound = true;
                            break;
                        }
                    }
                    if (!packageFound) {
                        Toast.makeText(ShareFragment.this.getActivity(), "Google+ not installed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ShareFragment.this.getActivity(), "Google+ not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.whatsapp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean packageFound = false;
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    if (ShareFragment.this.object.getImageUri() == null) {
                        intent.putExtra("android.intent.extra.TEXT", ShareFragment.this.shareString);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        intent.setType("text/plain");
                    } else {
                        intent.putExtra(Intent.EXTRA_STREAM, ShareFragment.this.object.getImageUri());
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(Intent.EXTRA_TEXT, ShareFragment.this.shareString);

                        intent.setType("image/*");
                    }
                    for (ResolveInfo info : ShareFragment.this.getActivity().getPackageManager().queryIntentActivities(intent, 0)) {
                        if (info.activityInfo.packageName.toLowerCase().startsWith("com.whatsapp")) {
                            intent.setPackage(info.activityInfo.packageName);
                            ShareFragment.this.startActivity(intent);
                            packageFound = true;
                            break;
                        }
                    }
                    if (!packageFound) {
                        Toast.makeText(ShareFragment.this.getActivity(), "Whatsapp not installed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ShareFragment.this.getActivity(), "Whatsapp not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.voiceOut = (Button) rootView.findViewById(R.id.voice_out);
        if (Utils.is15()) {
            this.voiceOut.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    VoiceOutDialog.newInstance(ShareFragment.this.object, shareString).show(ShareFragment.this.getChildFragmentManager(), "Dialog Fragment");

                }
            });
        } else {
            this.voiceOut.setVisibility(View.GONE);
        }
        this.more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                if (ShareFragment.this.object.getImageUri() == null) {
                    intent.putExtra("android.intent.extra.TEXT", ShareFragment.this.shareString);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    intent.setType("text/plain");
                } else {
                    intent.putExtra(Intent.EXTRA_STREAM, ShareFragment.this.object.getImageUri());
                    intent.putExtra(Intent.EXTRA_TEXT, ShareFragment.this.shareString);

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    intent.setType("image/*");
                }
                ShareFragment.this.startActivity(intent);
            }
        });
        try {
            this.shareText.setText(this.object.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    public void onStart() {
        super.onStart();
        this.mBehavior.setState(3);
    }

    private void shareToTwitterViaApp() {
        try {
            String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s", new Object[]{urlEncode(this.object.getTitle()), urlEncode(this.object.getUrl())});
            Intent intent = new Intent("android.intent.action.SEND");
            if (this.object.getImageUri() == null) {
                intent.putExtra("android.intent.extra.TEXT", this.shareString);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                intent.setType("text/plain");
            } else {
                intent.putExtra(Intent.EXTRA_STREAM, this.object.getImageUri());
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_TEXT, ShareFragment.this.shareString);

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

    private void shareToFacebookViaApp() {
        Intent intent = new Intent("android.intent.action.SEND");
        if (this.object.getImageUri() == null) {
            intent.putExtra("android.intent.extra.TEXT", this.shareString);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setType("text/plain");
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, this.object.getImageUri());
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_TEXT, ShareFragment.this.shareString);

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
            intent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + this.object.getUrl()));
        }
        startActivity(intent);
    }

    public static String urlEncode(String s) {
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }
}
