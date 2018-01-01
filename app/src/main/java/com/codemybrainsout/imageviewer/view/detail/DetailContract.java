package com.codemybrainsout.imageviewer.view.detail;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.codemybrainsout.imageviewer.model.Exif;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.view.base.BasePresenter;
import com.codemybrainsout.imageviewer.view.base.BaseView;

import retrofit2.http.Url;

/**
 * Created by ahulr on 09-06-2017.
 */

public class DetailContract {

    interface Presenter extends BasePresenter {

        void loadPhoto();

        void downloadPhoto(Context context, String url);

        void setAsWallpaper(Context context, String url);

        void showExif(Exif exif);

        void openUserDetails(User user);
    }

    interface View extends BaseView<Presenter> {

        void showPhoto(Photo photo);

        void showExif(Exif exif);

        void showUserDetails(User user);
    }

}
