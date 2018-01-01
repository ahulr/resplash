package com.codemybrainsout.imageviewer.view.collection;

import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.view.base.BasePresenter;
import com.codemybrainsout.imageviewer.view.base.BaseView;
import com.codemybrainsout.imageviewer.view.photo.PhotoContract;

import java.util.List;

/**
 * Created by ahulr on 09-06-2017.
 */

public class CollectionContract {

    interface Presenter extends BasePresenter {

        void openUser(User user);

        void loadCollections(int page, int limit);

        void loadMoreCollections(int page, int limit);

        void openCollection(Collection collection);
    }

    interface View extends BaseView<Presenter> {

        void showUser(User user);

        void showCollection(Collection collection);

        void refreshCollections(List<Collection> list);

        void addCollections(List<Collection> list);

    }

}
