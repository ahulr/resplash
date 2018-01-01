package com.codemybrainsout.imageviewer.view.home;

import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.view.base.BasePresenter;
import com.codemybrainsout.imageviewer.view.base.BaseView;

import java.util.List;

/**
 * Created by ahulr on 10-06-2017.
 */

public class HomeContract {

    interface Presenter extends BasePresenter {

        void loadPhotos(int limit, String orderBy);

        void loadCollections(int limit);

        void viewAllPhotos();

        void viewAllCollections();

        void openPhoto(Photo photo);

        void openCollection(Collection collection);

    }

    interface View extends BaseView<Presenter> {

        void setPhotos(List<Photo> list);

        void setCollections(List<Collection> list);

        void openAllCollections();

        void openAllPhotos();

        void showError(String s);

        void showPhoto(Photo photo);

        void showCollection(Collection collection);

    }

}
