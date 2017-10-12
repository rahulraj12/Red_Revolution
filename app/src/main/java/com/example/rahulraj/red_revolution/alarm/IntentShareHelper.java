package com.example.rahulraj.red_revolution.alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.rahulraj.red_revolution.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by sagardesai on 25/03/17.
 */
public class IntentShareHelper {

    public static void shareOnWhatsapp(AppCompatActivity appCompatActivity, String textBody, Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_TEXT,!TextUtils.isEmpty(textBody) ? textBody : "");

        if (fileUri != null) {
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
        }

        try {
            appCompatActivity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
            showWarningDialog(appCompatActivity, appCompatActivity.getString(R.string.error_activity_not_found));
        }
    }

    public static void shareOnTwitter(AppCompatActivity appCompatActivity, String textBody, Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.twitter.android");
        intent.putExtra(Intent.EXTRA_TEXT,!TextUtils.isEmpty(textBody) ? textBody : "");

        if (fileUri != null) {
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
        }

        try {
            appCompatActivity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
            showWarningDialog(appCompatActivity, appCompatActivity.getString(R.string.error_activity_not_found));
        }
    }

    public static void shareOnFacebook(AppCompatActivity appCompatActivity, String textBody, Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,!TextUtils.isEmpty(textBody) ? textBody : "");

        if (fileUri != null) {
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
        }

        boolean facebookAppFound = false;
        List<ResolveInfo> matches = appCompatActivity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana") ||
                    info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.lite")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

        if (facebookAppFound) {
            appCompatActivity.startActivity(intent);
        } else {
            showWarningDialog(appCompatActivity, appCompatActivity.getString(R.string.error_activity_not_found));
        }
    }

    private static void showWarningDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .create().show();
    }

    public static @Nullable
    Uri getUriFromFile(Context context, @Nullable File file) {
        if (file == null)
            return null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                return FileProvider.getUriForFile(context, "com.my.package.fileprovider", file);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return Uri.fromFile(file);
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public static Uri getLocalBitmapUri(Context context, EditText imageView) {
        Bitmap bmp = Bitmap.createBitmap(imageView.getDrawingCache());

//        Drawable drawable = imageView.getDrawable();
//        Bitmap bmp = null;
//        if (drawable instanceof BitmapDrawable) {
//            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//        } else {
//            return null;
//        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            bmpUri = getUriFromFile(context, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
