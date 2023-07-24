package com.sanskrit.pmo.Activities;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sanskrit.pmo.Fragments.GalleryPagerFragment;
import com.sanskrit.pmo.Fragments.ShareFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.ShareUtils;
import com.sanskrit.pmo.Utils.Util;
import com.sanskrit.pmo.network.mygov.models.gallery.ImageGallerySubModel;
import com.sanskrit.pmo.permissions.Nammu;
import com.sanskrit.pmo.permissions.PermissionCallback;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.ShareObject;
import com.sanskrit.pmo.utils.TintHelper;
import com.sanskrit.pmo.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;



import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GalleryPagerActivity extends BaseActivity {
   // FloatingActionsMenu fabMenu;
    FloatingActionButton fabSave;
   FloatingActionButton fabShare;
    FloatingActionButton fabWallaper;
    List<ImageGallerySubModel> objectList = new ArrayList();
    ProgressDialog pDialog;
    ViewPager pager;
    View parent;
    ImageView backBtn;
    private Uri shareUri;
    private boolean isDownloaded;

    static class Adapter extends FragmentPagerAdapter {
        private final List<String> mFragmentTitles = new ArrayList();
        private final List<Fragment> mFragments = new ArrayList();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragments.add(fragment);
            this.mFragmentTitles.add(title);
        }

        public Fragment getItem(int position) {
            return (Fragment) this.mFragments.get(position);
        }

        public int getCount() {
            return this.mFragments.size();
        }

        public CharSequence getPageTitle(int position) {
            return (CharSequence) this.mFragmentTitles.get(position);
        }
    }

    private class setWallpaper extends AsyncTask<Bitmap, Void, String> {
        private setWallpaper() {
        }

        protected String doInBackground(Bitmap... params) {
            try {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(GalleryPagerActivity.this.getApplicationContext());//.setBitmap(params[0]);

                wallpaperManager.setBitmap(params[0]);
                GalleryPagerActivity.this.pDialog.dismiss();
                Snackbar.make(GalleryPagerActivity.this.parent, (int) R.string.set_wallpaper_success, Snackbar.LENGTH_INDEFINITE).show();
            } catch (IOException e) {
                Snackbar.make(GalleryPagerActivity.this.parent, (int) R.string.error_setting_wallpaer, Snackbar.LENGTH_INDEFINITE).show();
                GalleryPagerActivity.this.pDialog.dismiss();
                e.printStackTrace();
            }
            return "Executed";
        }

        protected void onPostExecute(String result) {
        }

        protected void onPreExecute() {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);

        setContentView((int) R.layout.activity_gallery_pager);
        this.parent = findViewById(R.id.parent);
        this.pager = (ViewPager) findViewById(R.id.pager);

        backBtn = findViewById(R.id.back);

        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        this.fabWallaper = (FloatingActionButton) findViewById(R.id.fab_wallaper);
       // this.fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        this.fabShare = findViewById(R.id.fab_share);
        this.objectList = (List) Parcels.unwrap(getIntent().getParcelableExtra("image_list"));
        setupViewPager(this.pager);
        this.pager.setOffscreenPageLimit(2);
        this.fabWallaper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryPagerActivity.this.pDialog = new ProgressDialog(GalleryPagerActivity.this);
                GalleryPagerActivity.this.pDialog.setCancelable(false);
                GalleryPagerActivity.this.pDialog.setMessage(GalleryPagerActivity.this.getString(R.string.dialog_setting_wallpaer));
                GalleryPagerActivity.this.pDialog.show();
                ImageGallerySubModel gallerySubModel = (ImageGallerySubModel) GalleryPagerActivity.this.objectList.get(GalleryPagerActivity.this.pager.getCurrentItem());
                Picasso.with(GalleryPagerActivity.this).load(gallerySubModel.mUrl).into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, LoadedFrom from) {


                        new setWallpaper().execute(new Bitmap[]{bitmap});

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Snackbar.make(GalleryPagerActivity.this.parent, (int) R.string.error_setting_wallpaer, Snackbar.LENGTH_INDEFINITE).show();
                        GalleryPagerActivity.this.pDialog.dismiss();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

            }
        });
        this.fabSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isMarshmallow()) {
                    GalleryPagerActivity.this.checkPermissionAndThenSave(0);
                } else {
                    GalleryPagerActivity.this.saveToDevice();
                }
            }
        });
        this.fabShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isMarshmallow()) {
                    GalleryPagerActivity.this.checkPermissionAndThenSave(1);
                } else {
                    GalleryPagerActivity.this.share();
                }
            }
        });
        if (PreferencesUtility.getTheme(this).equals("black")) {
            this.fabShare.setImageDrawable(TintHelper.tintDrawable(this.fabShare.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));
        }
    }

    private void share() {

        // Delete Downloaded Image
        if (shareUri != null && !isDownloaded)
            Util.deleteFileFromUri(GalleryPagerActivity.this, shareUri);

        final ShareObject shareObject = new ShareObject();

        ImageGallerySubModel gallerySubModel = (ImageGallerySubModel) GalleryPagerActivity.this.objectList.get(GalleryPagerActivity.this.pager.getCurrentItem());

        Picasso.with(GalleryPagerActivity.this).load(gallerySubModel.mUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, LoadedFrom from) {

                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", null);
                shareUri = Uri.parse(path);
                shareObject.setImageUri(shareUri);

                shareObject.setTitle(((ImageGallerySubModel) GalleryPagerActivity.this.objectList.get(GalleryPagerActivity.this.pager.getCurrentItem())).mCaption);
                shareObject.setUrl(((ImageGallerySubModel) GalleryPagerActivity.this.objectList.get(GalleryPagerActivity.this.pager.getCurrentItem())).mUrl);
                ShareUtils su= new ShareUtils(GalleryPagerActivity.this,shareObject);
                su.shareToFacebookViaApp();
//                ShareFragment.newInstance(shareObject).show(GalleryPagerActivity.this.getSupportFragmentManager(), "Share Fragment");
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
            ImageGallerySubModel gallery = (ImageGallerySubModel) this.objectList.get(this.pager.getCurrentItem());
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Request req = new Request(Uri.parse(gallery.mUrl));
            req.setAllowedNetworkTypes(3).setTitle(gallery.mCaption).setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Image-" + String.valueOf(gallery.mUrl.hashCode()).substring(0, 4) + ".jpg");
            req.setNotificationVisibility(1);
            downloadManager.enqueue(req);
            Snackbar.make(this.parent, (int) R.string.downloading_image, Snackbar.LENGTH_SHORT).show();
            isDownloaded = true;
        } catch (Exception e) {
            Snackbar.make(this.parent, (CharSequence) "Some error occurred", Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        for (ImageGallerySubModel object : this.objectList) {
            GalleryPagerFragment galleryPagerFragment = new GalleryPagerFragment();
            adapter.addFragment(GalleryPagerFragment.newInstance(object), "Gallery");
        }
        viewPager.setAdapter(adapter);
        if (getIntent().getIntExtra(Constants.POSITION, 0) <= adapter.getCount()) {
            this.pager.setCurrentItem(getIntent().getIntExtra(Constants.POSITION, 0));
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_search:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void checkPermissionAndThenSave(int id) {

            if (id == 0) saveToDevice();
            else if (id == 1) share();

    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    final PermissionCallback permissionReadstorageCallback = new PermissionCallback() {
//        @Override
//        public void permissionGranted() {
//            GalleryPagerActivity.this.saveToDevice();
//        }
//
//        @Override
//        public void permissionRefused() {
//            Snackbar.make(GalleryPagerActivity.this.parent, (int) R.string.storage_permission_required, Snackbar.LENGTH_SHORT).show();
//
//        }
//    };

    @Override
    protected void onDestroy() {
        // Delete Downloaded Image
        if (shareUri != null && !isDownloaded)
            Util.deleteFileFromUri(GalleryPagerActivity.this, shareUri);
        super.onDestroy();
    }
}
