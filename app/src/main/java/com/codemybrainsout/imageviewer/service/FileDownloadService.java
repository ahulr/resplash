package com.codemybrainsout.imageviewer.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.codemybrainsout.imageviewer.BuildConfig;
import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.utility.Extra;
import com.codemybrainsout.imageviewer.utility.GenericFileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ahulr on 07-07-2017.
 */

public class FileDownloadService extends IntentService {

    // Used to identify when the IntentService finishes
    public static final String ACTION_DOWNLOAD_COMPLETE = "com.codemybrainsout.imageviewer.ACTION_DOWNLOAD_COMPLETE";
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private int id = 1;
    private boolean wallpaper = false;

    // Validates resource references inside Android XML files
    public FileDownloadService() {
        super(FileDownloadService.class.getName());
    }

    public FileDownloadService(String name) {
        super(name);
    }

    public static Intent getIntent(Context context, String url, boolean wallpaper) {
        Intent intent = new Intent(context, FileDownloadService.class);
        intent.putExtra(Extra.URL, url);
        intent.putExtra(Extra.WALLPAPER, wallpaper);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e("FileDownloadService", "Service Started");

        // Get the URL for the file to download
        String passedURL = intent.getStringExtra(Extra.URL);
        wallpaper = intent.getBooleanExtra(Extra.WALLPAPER, false);
        downloadFile(passedURL);
    }

    private void downloadFile(String url) {

        if (TextUtils.isEmpty(url)) {
            return;
        }

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Downloading image")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_download);
        mBuilder.setProgress(100, 100, true);
        // Displays the progress bar for the first time.
        mNotifyManager.notify(id, mBuilder.build());

        try {
            Bitmap bitmap = Glide.with(this).load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            if (bitmap != null) {
                if (wallpaper) {
                    setAsWallpaper(bitmap);
                } else {
                    saveImage(bitmap);
                }
            } else {
                failure();
            }

        } catch (InterruptedException e) {
            failure();
            e.printStackTrace();
        } catch (ExecutionException e) {
            failure();
            e.printStackTrace();
        }
    }

    private void saveImage(Bitmap bitmap) {
        //download direct to sd card

        File myDir;
        String root = Environment.getExternalStorageDirectory().toString();
        myDir = new File(root + "/Resplash");
        myDir.mkdirs();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String date = sdf.format(new Date());

        Uri contentUri = null;
        String fname = "Image-" + date + ".jpg";
        File file = new File(myDir, fname);
        //if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            File f = new File(myDir, fname);
            contentUri = GenericFileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", f);

            if (Build.VERSION.SDK_INT >= 19) {

                    /*Intent mediaScanIntent = new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);*/

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getPath());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
                getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            } else {
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.getPath()}, new String[]{"image/*"}, null);
            }

            updateNotification(contentUri, bitmap);
            success();

        } catch (Exception e) {
            e.printStackTrace();
            failure();
        }
    }

    private void updateNotification(Uri contentUri, Bitmap bitmap) {

        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.setDataAndType(contentUri, "image/*");
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(contentUri, "image/*");
        intent.putExtra("mimeType", "image/*");
        intent.putExtra("save_path", contentUri);
        givePermissionToEveryApp(intent, contentUri);
        PendingIntent setAsIntent = PendingIntent.getActivity(this, 1 /* Request code */, Intent.createChooser(intent, "Select service:"), PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        String title = "Resplash";
        String description = "Download complete";

        mBuilder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(description)
                .setStyle(new android.support.v7.app.NotificationCompat.BigPictureStyle().setBigContentTitle(title).bigPicture(bitmap).bigLargeIcon(icon).setSummaryText(description))
                .addAction(R.drawable.ic_wallpaper, "Set as Wallpaper", setAsIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 100, 100, 100, 100})
                .setSound(defaultSoundUri)
                .setPriority(android.support.v7.app.NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setProgress(0, 0, false)
                .setContentIntent(pendingIntent);

        mNotifyManager.notify(id /* ID of notification */, mBuilder.build());
    }

    private void success() {
        Log.e("FileDownloadService", "Service Stopped");

        // Broadcast an intent back to MainActivity when file is downloaded
        Intent i = new Intent(ACTION_DOWNLOAD_COMPLETE);
        i.putExtra(Extra.SUCCESS, true);
        if (wallpaper) {
            i.putExtra(Extra.WALLPAPER, true);
        }
        //FileService.this.sendBroadcast(i);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    private void failure() {
        Log.e("FileDownloadService", "Service Stopped");

        // Handle any errors
        mBuilder.setContentText("Download Failed")
                // Removes the progress bar
                .setOngoing(false)
                .setProgress(0, 0, false);
        mNotifyManager.notify(id, mBuilder.build());

        // Broadcast an intent back to MainActivity when file is downloaded
        Intent i = new Intent(ACTION_DOWNLOAD_COMPLETE);
        i.putExtra(Extra.SUCCESS, false);
        //FileService.this.sendBroadcast(i);
        if (wallpaper) {
            i.putExtra(Extra.WALLPAPER, true);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    private void setAsWallpaper(Bitmap bitmap) {
        mNotifyManager.cancel(id);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.setBitmap(bitmap);
            success();
        } catch (IOException e) {
            e.printStackTrace();
            failure();
        }
    }

    private void givePermissionToEveryApp(Intent intent, Uri imageFileUri) {
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, imageFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }
}