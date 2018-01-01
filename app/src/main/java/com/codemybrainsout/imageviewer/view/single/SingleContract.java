package com.codemybrainsout.imageviewer.view.single;

import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.view.base.BasePresenter;
import com.codemybrainsout.imageviewer.view.base.BaseView;
import com.codemybrainsout.imageviewer.view.search.SearchContract;

import java.util.List;

/**
 * Created by ahulr on 11-06-2017.
 */

public class SingleContract {

    public interface Presenter extends BasePresenter {

        void openUser(User user);

        void openPhoto(Photo photo);


        void loadCollection(Collection collection);

        void loadPhotos(int page, int limit);

        void loadMorePhotos(int page, int limit);

    }

    public interface View extends BaseView<Presenter> {

        void showUser(User user);

        void showPhoto(Photo photo);

        void showCollectionDetails(Collection collection);

        void refreshPhotos(List<Photo> list);

        void addPhotos(List<Photo> list);

    }

}
