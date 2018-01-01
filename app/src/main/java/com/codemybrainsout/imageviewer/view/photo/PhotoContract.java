package com.codemybrainsout.imageviewer.view.photo;

import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.view.base.BasePresenter;
import com.codemybrainsout.imageviewer.view.base.BaseView;

import java.util.List;

/**
 * Created by ahulr on 09-06-2017.
 */

public class PhotoContract {

    interface Presenter extends BasePresenter {

        void openUser(User user);

        void openPhoto(Photo photo);

        void loadPhotos(int page, int limit, String orderBy);

        void loadMorePhotos(int page, int limit, String orderBy);
    }

    interface View extends BaseView<Presenter> {

        void showUser(User user);

        void showPhoto(Photo photo);

        void refreshPhotos(List<Photo> list);

        void addPhotos(List<Photo> list);

    }

}
