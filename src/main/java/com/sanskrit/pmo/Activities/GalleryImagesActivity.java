package com.sanskrit.pmo.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sanskrit.pmo.Adapters.GalleryImagesAdapter;
import com.sanskrit.pmo.Fragments.ShareFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.ShareUtils;
import com.sanskrit.pmo.Utils.Util;
import com.sanskrit.pmo.network.mygov.models.gallery.ImageGalleryModel;
import com.sanskrit.pmo.utils.ShareObject;
import com.sanskrit.pmo.utils.TintHelper;
import com.sanskrit.pmo.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import org.parceler.Parcels;

import static com.sanskrit.pmo.utils.Utils.getImageUri;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class GalleryImagesActivity extends BaseActivity {
    GalleryImagesAdapter adapter;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton fabShare;
    ImageGalleryModel gallery = null;
    ImageView headerImage;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    Toolbar toolbar;
    private Uri shareUri;
    private String imagePath;
    private Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState) {
//        setFullscreen(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_images);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        this.appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        this.fabShare = (FloatingActionButton) findViewById(R.id.fab);
        this.headerImage = (ImageView) findViewById(R.id.header_picture);
        new StaggeredGridLayoutManager(2, 1).setGapStrategy(0);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.gallery = (ImageGalleryModel) Parcels.unwrap(getIntent().getParcelableExtra("gallery"));
        if (this.gallery != null) {
            setUpGallery();
        }
        if (PreferencesUtility.getTheme(this).equals("black")) {
            this.fabShare.setImageDrawable(TintHelper.tintDrawable(this.fabShare.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));
        }
        this.fabShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                // Delete Downloaded Image
                if (shareUri != null)
                    Util.deleteFileFromUri(GalleryImagesActivity.this, shareUri);

                shareUri = getImageUri(GalleryImagesActivity.this, bitmap);
                imagePath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, gallery.mTitle, null);

                ShareObject shareObject = new ShareObject();
                shareObject.setTitle(GalleryImagesActivity.this.gallery.mTitle);
                if (imagePath != null) {
                    shareUri = Uri.parse(imagePath);
                } else {

                }
                shareObject.setImageUri(shareUri);
                shareObject.setUrl(Utils.generatePmIndiaShareUrl(GalleryImagesActivity.this.gallery.mID));
                ShareUtils su= new ShareUtils(GalleryImagesActivity.this,shareObject);
                su.shareToFacebookViaApp();
//                ShareFragment.newInstance(shareObject).show(GalleryImagesActivity.this.getSupportFragmentManager(), "Share Fragment");


              /*  ShareObject shareObject = new ShareObject();
                shareObject.setTitle(GalleryImagesActivity.this.gallery.mTitle);
                shareObject.setUrl(Utils.generatePmIndiaShareUrl(GalleryImagesActivity.this.gallery.mID));
                ShareFragment.newInstance(shareObject).show(GalleryImagesActivity.this.getSupportFragmentManager(), "Share Fragment");*/

            }
        });
    }

    private void setUpGallery() {
        collapsingToolbarLayout.setTitle(this.gallery.mTitle);
        Picasso.with(this).load(this.gallery.mFeatureImage).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
                setBitmap(bitmap);
                GalleryImagesActivity.this.headerImage.setImageBitmap(bitmap);
                GalleryImagesActivity.this.setUpPaletteColors(bitmap);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        this.adapter = new GalleryImagesAdapter(this, this.gallery.mContent);
        this.recyclerView.setAdapter(this.adapter);
        this.progressBar.setVisibility(View.GONE);
    }

    private void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private void setUpPaletteColors(Bitmap loadedImage) {
        Palette.generateAsync(loadedImage, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int textColor;
                int color = palette.getVibrantColor(Color.parseColor("#66000000"));
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch != null) {
                    textColor = GalleryImagesActivity.getOpaqueColor(swatch.getTitleTextColor());
                } else {
                    textColor = Color.parseColor("#ffffff");
                }
                GalleryImagesActivity.this.collapsingToolbarLayout.setContentScrimColor(color);
                GalleryImagesActivity.this.collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(Color.parseColor("#66000000")));
                GalleryImagesActivity.this.collapsingToolbarLayout.setCollapsedTitleTextColor(textColor);
            }
        });
        Palette palette = Palette.generate(loadedImage);
        this.collapsingToolbarLayout.setContentScrimColor(palette.getVibrantColor(Color.parseColor("#66000000")));
        this.collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(Color.parseColor("#66000000")));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            case R.id.action_search:
                // Toast.makeText(QuotesActivity.this, "call", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SearchGalleryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static int getOpaqueColor(@ColorInt int paramInt) {
        return ViewCompat.MEASURED_STATE_MASK | paramInt;
    }

   /* @Override
    protected void onResume() {
        hideSoftKeyboard(GalleryImagesActivity.this);
        super.onResume();
    }
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }*/

    @Override
    protected void onDestroy() {
        // Delete Downloaded Image
        if (shareUri != null)
            Util.deleteFileFromUri(GalleryImagesActivity.this, shareUri);
        super.onDestroy();
    }
}
