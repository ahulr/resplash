package com.codemybrainsout.imageviewer.service

import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.codemybrainsout.imageviewer.BuildConfig
import com.codemybrainsout.imageviewer.R
import com.codemybrainsout.imageviewer.utility.Extra
import com.codemybrainsout.imageviewer.utility.GenericFileProvider
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutionException

/**
 * Created by ahulr on 07-07-2017.
 */
class FileDownloadService(name: String?) : IntentService(name) {

    private var mNotifyManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null
    private val id = 1
    private var wallpaper = false

    override fun onHandleIntent(intent: Intent?) {
        Timber.e("Service Started")

        // Get the URL for the file to download
        val passedURL: String = intent.getStringExtra(Extra.URL)
        wallpaper = intent.getBooleanExtra(Extra.WALLPAPER, false)
        downloadFile(passedURL)
    }

    private fun downloadFile(url: String) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        mBuilder = NotificationCompat.Builder(this)
        mBuilder.setContentTitle(getResources().getString(R.string.app_name))
            .setContentText("Downloading image")
            .setOngoing(true)
            .setPriority(Notification.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_download)
        mBuilder.setProgress(100, 100, true)
        // Displays the progress bar for the first time.
        mNotifyManager.notify(id, mBuilder.build())
        try {
            val bitmap: Bitmap = Glide.with(this).load(url).asBitmap()
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
            if (bitmap != null) {
                if (wallpaper) {
                    setAsWallpaper(bitmap)
                } else {
                    saveImage(bitmap)
                }
            } else {
                failure()
            }
        } catch (e: InterruptedException) {
            failure()
            e.printStackTrace()
        } catch (e: ExecutionException) {
            failure()
            e.printStackTrace()
        }
    }

    private fun saveImage(bitmap: Bitmap) {
        //download direct to sd card
        val myDir: File
        val root: String = Environment.getExternalStorageDirectory().toString()
        myDir = File("$root/Resplash")
        myDir.mkdirs()
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val date = sdf.format(Date())
        var contentUri: Uri? = null
        val fname = "Image-$date.jpg"
        val file = File(myDir, fname)
        //if (file.exists()) file.delete();
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            val f = File(myDir, fname)
            contentUri = GenericFileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID.toString() + ".provider",
                f
            )
            if (Build.VERSION.SDK_INT >= 19) {

                /*Intent mediaScanIntent = new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);*/
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, file.path)
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
                getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                MediaScannerConnection.scanFile(
                    getApplicationContext(),
                    arrayOf(file.path),
                    arrayOf("image/*"),
                    null
                )
            }
            updateNotification(contentUri, bitmap)
            success()
        } catch (e: Exception) {
            e.printStackTrace()
            failure()
        }
    }

    private fun updateNotification(contentUri: Uri?, bitmap: Bitmap) {
        val i = Intent()
        i.setAction(Intent.ACTION_VIEW)
        i.setDataAndType(contentUri, "image/*")
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0 /* Request code */,
            i,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val intent = Intent(Intent.ACTION_ATTACH_DATA)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.setDataAndType(contentUri, "image/*")
        intent.putExtra("mimeType", "image/*")
        intent.putExtra("save_path", contentUri)
        givePermissionToEveryApp(intent, contentUri)
        val setAsIntent: PendingIntent = PendingIntent.getActivity(
            this,
            1 /* Request code */,
            Intent.createChooser(intent, "Select service:"),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val icon: Bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
        val title = "Resplash"
        val description = "Download complete"
        mBuilder.setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(description)
            .setStyle(
                Notification.BigPictureStyle().setBigContentTitle(title).bigPicture(bitmap)
                    .bigLargeIcon(icon)
                    .setSummaryText(description)
            )
            .addAction(R.drawable.ic_wallpaper, "Set as Wallpaper", setAsIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 100, 100, 100, 100))
            .setSound(defaultSoundUri)
            .setPriority(android.support.v7.app.NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(false)
            .setProgress(0, 0, false)
            .setContentIntent(pendingIntent)
        mNotifyManager.notify(id /* ID of notification */, mBuilder.build())
    }

    private fun success() {
        Timber.e("Service Stopped")

        // Broadcast an intent back to MainActivity when file is downloaded
        val i = Intent(ACTION_DOWNLOAD_COMPLETE)
        i.putExtra(Extra.SUCCESS, true)
        if (wallpaper) {
            i.putExtra(Extra.WALLPAPER, true)
        }
        //FileService.this.sendBroadcast(i);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i)
    }

    private fun failure() {
        Timber.e("Service Stopped")

        // Handle any errors
        mBuilder.setContentText("Download Failed") // Removes the progress bar
            .setOngoing(false)
            .setProgress(0, 0, false)
        mNotifyManager.notify(id, mBuilder.build())

        // Broadcast an intent back to MainActivity when file is downloaded
        val i = Intent(ACTION_DOWNLOAD_COMPLETE)
        i.putExtra(Extra.SUCCESS, false)
        //FileService.this.sendBroadcast(i);
        if (wallpaper) {
            i.putExtra(Extra.WALLPAPER, true)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(i)
    }

    private fun setAsWallpaper(bitmap: Bitmap) {
        mNotifyManager.cancel(id)
        val wallpaperManager: WallpaperManager =
            WallpaperManager.getInstance(getApplicationContext())
        try {
            wallpaperManager.setBitmap(bitmap)
            success()
        } catch (e: IOException) {
            e.printStackTrace()
            failure()
        }
    }

    private fun givePermissionToEveryApp(intent: Intent, imageFileUri: Uri?) {
        val resInfoList: List<ResolveInfo> =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName: String = resolveInfo.activityInfo.packageName
            grantUriPermission(
                packageName,
                imageFileUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

    companion object {
        // Used to identify when the IntentService finishes
        const val ACTION_DOWNLOAD_COMPLETE =
            "com.codemybrainsout.imageviewer.ACTION_DOWNLOAD_COMPLETE"

        @JvmStatic
        fun getIntent(context: Context?, url: String?, wallpaper: Boolean): Intent {
            val intent = Intent(context, FileDownloadService::class.java)
            intent.putExtra(Extra.URL, url)
            intent.putExtra(Extra.WALLPAPER, wallpaper)
            return intent
        }
    }
}