package com.sanskrit.pmo.Activities;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sanskrit.pmo.Fragments.ShareFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.ShareUtils;
import com.sanskrit.pmo.Utils.TouchImageView;
import com.sanskrit.pmo.Utils.Util;
import com.sanskrit.pmo.network.mygov.models.ImageObject;
import com.sanskrit.pmo.permissions.Nammu;
import com.sanskrit.pmo.permissions.PermissionCallback;
import com.sanskrit.pmo.utils.ShareObject;
import com.sanskrit.pmo.utils.TintHelper;
import com.sanskrit.pmo.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;



import org.parceler.Parcels;

public class ViewImageActivity extends BaseActivity {
    FloatingActionButton fabSave;
    ImageObject imageObject;
    TextView imageText;
    TouchImageView imageView;
    ImageView imageViewBackButton;
    View parent;
    FloatingActionButton fabShare;
    private Uri shareUri;
    private int taskID;
    private boolean isDownloaded;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        this.imageView = (TouchImageView) findViewById(R.id.touch_image);
        this.imageViewBackButton = (ImageView) findViewById(R.id.back);
        this.fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        this.fabShare = (FloatingActionButton) findViewById(R.id.fab_share);
        if (PreferencesUtility.getTheme(this).equals("black")) {
            this.fabShare.setImageDrawable(TintHelper.tintDrawable(this.fabShare.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));
        }
        this.imageText = (TextView) findViewById(R.id.image_text);
        this.parent = findViewById(R.id.container);
        this.imageObject = (ImageObject) Parcels.unwrap(getIntent().getParcelableExtra("image"));
        try {
            Picasso.with(this).load(this.imageObject.getUrl()).placeholder((int) R.drawable.ic_image_black_36dp).error((int) R.drawable.ic_image_black_36dp).into(this.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageViewBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        this.imageText.setText(Html.fromHtml(this.imageObject.getTitle()));
        this.fabSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                taskID = 0;
                if (Utils.isMarshmallow()) {
                    ViewImageActivity.this.checkPermissionAndThenSave();
                } else {
                    ViewImageActivity.this.saveToDevice();
                }
            }
        });


        fabShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                taskID = 1;
                if (Utils.isMarshmallow()) {
                    ViewImageActivity.this.checkPermissionAndThenSave();
                } else {
                    ViewImageActivity.this.share();
                }

            }
        });
    }

    private void share() {

        // Delete Downloaded Image
        if (shareUri != null && !isDownloaded)
            Util.deleteFileFromUri(ViewImageActivity.this, shareUri);

        final ShareObject shareObject = new ShareObject();
        shareObject.setTitle(imageObject.getTitle());

        Picasso.with(ViewImageActivity.this).load(imageObject.getUrl()).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                //shareUri = getImageUri(ViewImageActivity.this,bitmap);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, imageObject.getTitle(), null);
                if (path != null) {
                    shareUri = Uri.parse(path);
                } else {
                }
                shareObject.setImageUri(shareUri);
                shareObject.setImageUri(shareUri);
                if (getIntent().getBooleanExtra("Quote", false) || getIntent().getBooleanExtra("Infographics", false)) {
                    shareObject.setUrl(imageObject.getUrl());
                } else
                    shareObject.setUrl(Utils.generatePmIndiaShareUrl(imageObject.getId()));
                ShareUtils su= new ShareUtils(ViewImageActivity.this,shareObject);
                su.shareToFacebookViaApp();
//                ShareFragment.newInstance(shareObject).show(getSupportFragmentManager(), "Share Fragment");
                isDownloaded = false;

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void saveToDevice() {
        try {
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Request req = new Request(Uri.parse(this.imageObject.getUrl()));
            req.setAllowedNetworkTypes(3).setTitle(this.imageObject.getTitle()).setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Image-" + this.imageObject.getId() + ".jpg");
            req.setNotificationVisibility(1);
            downloadManager.enqueue(req);
            Snackbar.make(this.parent, (int) R.string.downloading_image, Snackbar.LENGTH_SHORT).show();
            isDownloaded = true;
        } catch (Exception e) {
            Snackbar.make(this.parent, (CharSequence) "Some error occurred", Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void checkPermissionAndThenSave() {
//        if (Nammu.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
            if (taskID == 0) {
                saveToDevice();
            } else if (taskID == 1) {
                share();
            }
//        } else if (Nammu.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
//            Snackbar.make(this.parent, getString(R.string.storage_permission_required), Snackbar.LENGTH_INDEFINITE).setAction((CharSequence) "OK", new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Nammu.askForPermission(ViewImageActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE", ViewImageActivity.this.permissionReadstorageCallback);
//
//                }
//            }).show();
//        } else {
//            Nammu.askForPermission((Activity) this, "android.permission.WRITE_EXTERNAL_STORAGE", this.permissionReadstorageCallback);
//        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);ƒ
//        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//
//    final PermissionCallback permissionReadstorageCallback = new PermissionCallback() {
//        @Override
//        public void permissionGranted() {
//            ViewImageActivity.this.saveToDevice();
//        }
//
//        @Override
//        public void permissionRefused() {
//            Snackbar.make(ViewImageActivity.this.parent, ViewImageActivity.this.getString(R.string.storage_permission_required), Snackbar.LENGTH_SHORT).show();
//
//        }
//    };

    @Override
    protected void onDestroy() {
        // Delete Downloaded Image
        if (shareUri != null && !isDownloaded)
            Util.deleteFileFromUri(ViewImageActivity.this, shareUri);
        super.onDestroy();
    }

}
