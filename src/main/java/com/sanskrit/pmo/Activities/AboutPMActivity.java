package com.sanskrit.pmo.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sanskrit.pmo.Fragments.ShareFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.ShareUtils;
import com.sanskrit.pmo.Utils.Util;
import com.sanskrit.pmo.network.mygov.MyGovCacheClient;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.models.KnowPM;
import com.sanskrit.pmo.permissions.Nammu;
import com.sanskrit.pmo.permissions.PermissionCallback;
import com.sanskrit.pmo.utils.ShareObject;
import com.sanskrit.pmo.utils.TintHelper;
import com.sanskrit.pmo.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

import static com.sanskrit.pmo.utils.Utils.getImageUri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.palette.graphics.Palette;

public class AboutPMActivity extends BaseActivity {
    AppBarLayout appBarLayout;
    CardView cardView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout coordinatorLayout;
    TextView description;
    FloatingActionButton fabShare;
    ImageView headerImage;
    SmoothProgressBar progressBar;
    Toolbar toolbar;
    private Uri shareUri;
    private KnowPM pm;
    private Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_about_pm);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        this.appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        this.headerImage = (ImageView) findViewById(R.id.header_picture);
        this.description = (TextView) findViewById(R.id.description);
        this.cardView = (CardView) findViewById(R.id.cardView);
        this.progressBar = (SmoothProgressBar) findViewById(R.id.progressBar);
        this.fabShare = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.collapsingToolbarLayout.setTitle(getString(R.string.know_the_pm));
        fetchDetails();
        if (PreferencesUtility.getTheme(this).equals("black")) {
            this.fabShare.setImageDrawable(TintHelper.tintDrawable(this.fabShare.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));
        }
    }

    private void fetchDetails() {
        MyGovCacheClient.getInstance(this).getPMProfile(PreferencesUtility.getLanguagePrefernce(this), new GenericCallback() {


            @Override
            public void success(Object response) {

                if (response instanceof KnowPM) {
                    AboutPMActivity.this.progressBar.setVisibility(View.GONE);
                    pm = (KnowPM) response;
                    AboutPMActivity.this.collapsingToolbarLayout.setTitle(pm.mTitle);
                    //String content=pm.mCOntent.replace("http://","\nhttp://").replace("https://","\nhttps://");
                    try {

                        AboutPMActivity.this.description.setText(Html.fromHtml(pm.mCOntent.replaceAll("(<(/)img>)|(<img.+?>)", "").replace("<a", "<br/><a")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        AboutPMActivity.this.description.setText(Html.fromHtml(pm.mCOntent));
                    }
                    AboutPMActivity.this.description.setMovementMethod(LinkMovementMethod.getInstance());
                    AboutPMActivity.this.cardView.setVisibility(View.VISIBLE);

                    Picasso.with(AboutPMActivity.this).load(pm.mImage).into(headerImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //You will get your bitmap here
                                    bitmap = ((BitmapDrawable) headerImage.getDrawable()).getBitmap();
                                    AboutPMActivity.this.headerImage.setImageBitmap(bitmap);
                                    if (bitmap != null)
                                        shareUri = getImageUri(AboutPMActivity.this, bitmap);

                                    AboutPMActivity.this.setUpPaletteColors(bitmap);
                                }
                            }, 100);
                        }

                        @Override
                        public void onError() {

                        }
                    });


                    AboutPMActivity.this.fabShare.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
//                            if (Utils.isMarshmallow()) {
//                                checkPermissionAndThenShare();
//                            } else {
                                share();
//                            }
                        }
                    });
                }
            }


            @Override
            public void failure() {

            }
        });


    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void checkPermissionAndThenShare() {
//        if (Nammu.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
//            share();
//        } else if (Nammu.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
//            Snackbar.make(findViewById(R.id.container), getString(R.string.storage_permission_required), Snackbar.LENGTH_INDEFINITE).setAction((CharSequence) "OK", new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Nammu.askForPermission(AboutPMActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE", AboutPMActivity.this.permissionReadstorageCallback);
//
//                }
//            }).show();
//        } else {
//            Nammu.askForPermission((Activity) this, "android.permission.WRITE_EXTERNAL_STORAGE", this.permissionReadstorageCallback);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//
//    final PermissionCallback permissionReadstorageCallback = new PermissionCallback() {
//        @Override
//        public void permissionGranted() {
//            share();
//        }
//
//        @Override
//        public void permissionRefused() {
//            Snackbar.make(findViewById(R.id.container), AboutPMActivity.this.getString(R.string.storage_permission_required), Snackbar.LENGTH_SHORT).show();
//
//        }
//    };

    private void share() {
        // Delete Downloaded Image
        if (shareUri != null)
            Util.deleteFileFromUri(AboutPMActivity.this, shareUri);
        /*ShareObject shareObject = new ShareObject();
        shareObject.setTitle("Know the PM");
        shareUri=Uri.parse(imagePath);
        shareObject.setImageUri(shareUri);

        shareObject.setUrl(pm.mLink);
        ShareFragment.newInstance(shareObject).show(AboutPMActivity.this.getSupportFragmentManager(),
                "Share Fragment");*/

        final ShareObject shareObject = new ShareObject();
        shareObject.setTitle("Know the PM");

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, AboutPMActivity.this.pm.mTitle, null);
        shareUri = Uri.parse(path);
        shareObject.setImageUri(shareUri);
        try {
            shareObject.setUrl(Utils.generatePmIndiaShareUrl(AboutPMActivity.this.pm.mLink));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ShareUtils su= new ShareUtils(AboutPMActivity.this,shareObject);
        su.shareToFacebookViaApp();
//        ShareFragment.newInstance(shareObject).show(AboutPMActivity.this.getSupportFragmentManager(), "Share Fragment");


        /*final ShareObject shareObject = new ShareObject();
        shareObject.setTitle("Know the PM");
        Picasso.with(AboutPMActivity.this).load(pm.mImage).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, AboutPMActivity.this.pm.mTitle, null);
                shareUri = Uri.parse(path);
                shareObject.setImageUri(shareUri);
                shareObject.setUrl(Utils.generatePmIndiaShareUrl(AboutPMActivity.this.pm.mLink));
                ShareFragment.newInstance(shareObject).show(AboutPMActivity.this.getSupportFragmentManager(), "Share Fragment");

                // Delete Downloaded Image
                Util.deleteFileFromUri(AboutPMActivity.this, shareUri);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });*/
    }

    private void setUpPaletteColors(Bitmap loadedImage) {
        try {
            Palette palette = Palette.generate(loadedImage);
            this.collapsingToolbarLayout.setContentScrimColor(palette.getVibrantColor(Color.parseColor("#66000000")));
            this.collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(Color.parseColor("#66000000")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        // Delete Downloaded Image
        if (shareUri != null)
            Util.deleteFileFromUri(AboutPMActivity.this, shareUri);
        super.onDestroy();
    }
}
