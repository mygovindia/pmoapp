package com.sanskrit.pmo.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    Activity context;
    TextView customwish;
    TextView customwishsubtext;
    FloatingActionButton fab;
    int gResId;
    View rootview;
    String title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_custom_wishes);
        this.customwish = (TextView) findViewById(R.id.custom_wish);
        this.customwishsubtext = (TextView) findViewById(R.id.custom_wish_subtext);
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.rootview = findViewById(R.id.rootview);
        this.context = this;
        String action = getIntent().getAction();

        if (action.equals("anniversary")) {
            TypedArray imgs2 = getResources().obtainTypedArray(R.array.anniversary_pics);
            int resID2 = imgs2.getResourceId(new SecureRandom().nextInt(imgs2.length()), 0);
            this.rootview.setBackgroundResource(resID2);
            this.gResId = resID2;
            imgs2.recycle();
        } else if (action.equals("birthday")) {
            TypedArray imgs = getResources().obtainTypedArray(R.array.birthday_pics);
            int resID = imgs.getResourceId(new SecureRandom().nextInt(imgs.length()), 0);
            this.rootview.setBackgroundResource(resID);
            this.gResId = resID;
            imgs.recycle();
        }


        this.fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomWishActivity.this.title = "";
                String action = CustomWishActivity.this.getIntent().getAction();

                if (action.equals("anniversary")) {
                    CustomWishActivity.this.title = CustomWishActivity.this.getString(R.string.wish_anniversary_title);

                } else if (action.equals("birthday")) {
                    CustomWishActivity.this.title = CustomWishActivity.this.getString(R.string.wish_birthday_title);

                }

                new DrawMultiLine().execute();
            }
        });
    }

    public Bitmap drawMultilineTextToBitmap(Context gContext, int gResId, String gText) {
        try {
            Resources resources = gContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);
            Config bitmapConfig = bitmap.getConfig();
            if (bitmapConfig == null) {
                bitmapConfig = Config.ARGB_8888;
            }
            bitmap = bitmap.copy(bitmapConfig, true);
            TextPaint paint = new TextPaint(1);
            paint.setAntiAlias(true);
            paint.setColor(-1);
            paint.setTextSize((float) ((int) (8.0f * scale)));
            int textWidth = bitmap.getWidth() - ((int) (16.0f * scale));
            StaticLayout textLayout = new StaticLayout(gText, paint, textWidth, Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            Bitmap bitmapExtended = Bitmap.createBitmap(bitmap.getWidth(), (bitmap.getHeight() + textLayout.getHeight()) + ((int) (20.0f * scale)), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapExtended);
            canvas.drawColor(Color.parseColor("#3c328b"));
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, new Paint());
            float x = (float) ((bitmap.getWidth() - textWidth) / 2);
            float y = (float) (bitmap.getHeight() + 15);
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
            Log.d("Share", "Error creating media file, check storage permissions: ");
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            inImage.compress(CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("Share", "File not found: " + e.getMessage());
        } catch (IOException e2) {
            Log.d("Share", "Error accessing file: " + e2.getMessage());
        }
        return Uri.fromFile(pictureFile);
    }

    private File getOutputMediaFile() {
        try {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName() + "/Files");
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                return null;
            }
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "tempshare.jpg");
            if (!mediaFile.exists()) {
                return mediaFile;
            }
            mediaFile.delete();
            return mediaFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Bitmap checkPermissionAndThenShare(Context gContext, int gResId, String gText) {
        //if (Nammu.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
            return drawMultilineTextToBitmap(gContext, gResId, gText);
//        }
//        Nammu.askForPermission(this.context, "android.permission.WRITE_EXTERNAL_STORAGE", this.permissionReadstorageCallback);
//        return null;
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }


    class DrawMultiLine extends AsyncTask<Void, Void, Bitmap> {
        DrawMultiLine() {
        }

        protected Bitmap doInBackground(Void... params) {
            if (Utils.isMarshmallow()) {
                return CustomWishActivity.this.checkPermissionAndThenShare(CustomWishActivity.this.context, CustomWishActivity.this.gResId, CustomWishActivity.this.title);
            }
            return CustomWishActivity.this.drawMultilineTextToBitmap(CustomWishActivity.this.context, CustomWishActivity.this.gResId, CustomWishActivity.this.title);
        }

        protected void onPostExecute(Bitmap bitmap) {
            ShareObject shareObject = new ShareObject();
            shareObject.setTitle(CustomWishActivity.this.title);
            shareObject.setUrl("");
            if (bitmap != null) {
                shareObject.setImageUri(CustomWishActivity.this.getImageUri(CustomWishActivity.this.context, bitmap));
            }
            ShareUtils su= new ShareUtils(CustomWishActivity.this,shareObject);
            su.shareToFacebookViaApp();
//            ShareFragment.newInstance(shareObject).show(CustomWishActivity.this.getSupportFragmentManager(), "Share Fragment");

        }
    }

//    PermissionCallback permissionReadstorageCallback = new PermissionCallback() {
//        @Override
//        public void permissionGranted() {
//            try {
//                ((DialogFragment) CustomWishActivity.this.getSupportFragmentManager().findFragmentByTag("Share Fragment")).dismiss();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void permissionRefused() {
//            Toast.makeText(CustomWishActivity.this.context, CustomWishActivity.this.context.getString(R.string.storage_permission_required), Toast.LENGTH_SHORT).show();
//
//        }
//    };

}
