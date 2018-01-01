package com.codemybrainsout.imageviewer.view.detail;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.api.PhotoService;
import com.codemybrainsout.imageviewer.model.Exif;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.service.FileDownloadService;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Url;
import timber.log.Timber;

/**
 * Created by ahulr on 09-06-2017.
 */

public class DetailPresenter implements DetailContract.Presenter {

    private final DetailContract.View mView;
    private final Photo mPhoto;
    private PhotoService mPhotoService;

    public DetailPresenter(PhotoService photoService, @NonNull DetailContract.View view, Photo photo) {
        mView = view;
        mPhoto = photo;
        mPhotoService = photoService;
    }

    @Override
    public void loadPhoto() {
        mView.showLoading();
        mPhotoService.getSinglePhoto(mPhoto.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Photo>() {

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showError(e.getMessage());
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(Photo photo) {
                        mView.showPhoto(photo);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void downloadPhoto(Context context, String url) {
        Intent intent = FileDownloadService.getIntent(context, url,false);
        context.startService(intent);
    }

    @Override
    public void setAsWallpaper(Context context, String url) {
        Intent intent = FileDownloadService.getIntent(context, url,true);
        context.startService(intent);
    }

    @Override
    public void showExif(Exif exif) {
        mView.showExif(exif);
    }

    @Override
    public void openUserDetails(User user) {
        mView.showUserDetails(user);
    }
}
