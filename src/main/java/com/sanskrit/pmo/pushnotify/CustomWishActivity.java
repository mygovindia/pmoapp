package com.sanskrit.pmo.pushnotify;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sanskrit.pmo.Activities.NewsReaderActivity;
import com.sanskrit.pmo.Fragments.ShareFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Utils.ShareUtils;
import com.sanskrit.pmo.permissions.Nammu;
import com.sanskrit.pmo.permissions.PermissionCallback;
import com.sanskrit.pmo.utils.ShareObject;
import com.sanskrit.pmo.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;


public class CustomWishActivity extends AppCompatActivity {

    TextView customwish, customwishsubtext;
    View rootview;
    FloatingActionButton fab;

    Activity context;
    int gResId;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_wishes);

        customwish = (TextView) findViewById(R.id.custom_wish);
        customwishsubtext = (TextView) findViewById(R.id.custom_wish_subtext);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        rootview = findViewById(R.id.rootview);
        context = this;

        switch (getIntent().getAction()) {
            case "birthday":
                final TypedArray imgs = getResources().obtainTypedArray(R.array.birthday_pics);
                final SecureRandom rand = new SecureRandom();
                final int rndInt = rand.nextInt(imgs.length());
                final int resID = imgs.getResourceId(rndInt, 0);
                rootview.setBackgroundResource(resID);
                gResId = resID;
                imgs.recycle();
                break;
            case "anniversary":
                final TypedArray imgs2 = getResources().obtainTypedArray(R.array.anniversary_pics);
                final SecureRandom rand2 = new SecureRandom();
                final int rndInt2 = rand2.nextInt(imgs2.length());
                final int resID2 = imgs2.getResourceId(rndInt2, 0);
                rootview.setBackgroundResource(resID2);
                gResId = resID2;
                imgs2.recycle();
                break;
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "";
                switch (getIntent().getAction()) {
                    case "birthday":
                        title = getString(R.string.wish_birthday_title);
                        break;
                    case "anniversary":
                        title = getString(R.string.wish_anniversary_title);
                        break;
                }

                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        if (Utils.isMarshmallow())
                            return checkPermissionAndThenShare(context, gResId, title);
                        else
                            return drawMultilineTextToBitmap(context, gResId, title);
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        ShareObject shareObject = new ShareObject();
                        shareObject.setTitle(title);
                        shareObject.setUrl("");
                        if (bitmap != null) {
                            shareObject.setImageUri(getImageUri(context, bitmap));
                        }
                        ShareUtils su= new ShareUtils(CustomWishActivity.this,shareObject);
                        su.shareToFacebookViaApp();
//                        ShareFragment fragment = ShareFragment.newInstance(shareObject);
//                        fragment.show(getSupportFragmentManager(), "Share Fragment");
                    }
                }.execute();
            }
        });
    }

    public Bitmap drawMultilineTextToBitmap(Context gContext,
                                            int gResId,
                                            String gText) {

        try {
            // prepare canvas
            Resources resources = gContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

            Bitmap.Config bitmapConfig = bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }

            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            // new antialiased Paint
            TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            // text size in pixels
            paint.setTextSize((int) (8 * scale));

            // set text width to canvas width minus 16dp padding
            int textWidth = bitmap.getWidth() - (int) (16 * scale);

            // init StaticLayout for text
            StaticLayout textLayout = new StaticLayout(
                    gText, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

            // get height of multiline text
            int textHeight = textLayout.getHeight();


            Bitmap bitmapExtended = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight() + textHeight + (int) (20 * scale), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapExtended);
            canvas.drawColor(Color.parseColor("#3c328b"));
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
            // get position of text's top left corner
            float x = (bitmap.getWidth() - textWidth) / 2;
            float y = (bitmap.getHeight() + 15);

            // draw text to the Canvas center
            canvas.save();
            canvas.translate(x, y);
            textLayout.draw(canvas);
            canvas.restore();

            return bitmapExtended;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
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

    private File getOutputMediaFile() {
        try {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Bitmap checkPermissionAndThenShare(Context gContext,
                                               int gResId,
                                               String gText) {
        //check for permission
//        if (Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return drawMultilineTextToBitmap(gContext, gResId, gText);
//        } else {
//            Nammu.askForPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionReadstorageCallback);
//        }
//        return null;
    }

//    final PermissionCallback permissionReadstorageCallback = new PermissionCallback() {
//        @Override
//        public void permissionGranted() {
//            try {
//                DialogFragment fragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag("Share Fragment");
//                fragment.dismiss();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void permissionRefused() {
//            Toast.makeText(context, context.getString(R.string.storage_permission_required), Toast.LENGTH_SHORT).show();
//        }
//    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
