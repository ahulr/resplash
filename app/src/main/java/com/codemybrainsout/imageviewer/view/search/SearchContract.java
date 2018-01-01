package com.codemybrainsout.imageviewer.view.search;

import android.view.View;

import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.view.base.BasePresenter;
import com.codemybrainsout.imageviewer.view.base.BaseView;

import java.util.List;

/**
 * Created by ahulr on 11-06-2017.
 */

public class SearchContract {

    public interface Presenter extends BasePresenter {

        void openUser(User user);

        void openPhoto(Photo photo);

        void searchPhotos(String query, int page, int limit);

        void searchMorePhotos(String query, int page, int limit);

    }

    public interface View extends BaseView<Presenter> {

        void showUser(User user);

        void showPhoto(Photo photo);

        void refreshPhotos(List<Photo> list);

        void addPhotos(List<Photo> list);
    }

}
