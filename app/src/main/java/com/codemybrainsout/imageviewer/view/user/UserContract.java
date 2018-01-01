package com.codemybrainsout.imageviewer.view.user;

import com.codemybrainsout.imageviewer.model.Exif;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.view.base.BasePresenter;
import com.codemybrainsout.imageviewer.view.base.BaseView;
import com.codemybrainsout.imageviewer.view.detail.DetailContract;

import java.util.List;

/**
 * Created by ahulr on 10-06-2017.
 */

public class UserContract {

    interface Presenter extends BasePresenter {

        void loadUser(String username);

        void loadPhotos(String username, int page, int limit);

        void loadMorePhotos(String username, int page, int limit);

        void openPhotoDetails(Photo photo);

    }

    interface View extends BaseView<Presenter> {

        void showUser(User user);

        void refreshPhotos(List<Photo> list);

        void addPhotos(List<Photo> list);

        void showPhotoDetails(Photo photo);

    }
}
