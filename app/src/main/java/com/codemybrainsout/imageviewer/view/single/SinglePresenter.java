package com.codemybrainsout.imageviewer.view.single;

import com.codemybrainsout.imageviewer.api.CollectionService;
import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ahulr on 11-06-2017.
 */

public class SinglePresenter implements SingleContract.Presenter {

    private Collection collection;
    public CollectionService mCollectionService;
    public SingleContract.View mView;

    public SinglePresenter(CollectionService collectionService, SingleContract.View view, Collection collection) {
        this.mCollectionService = collectionService;
        this.mView = view;
        this.collection = collection;
    }

    @Override
    public void openUser(User user) {
        mView.showUser(user);
    }

    @Override
    public void openPhoto(Photo photo) {
        mView.showPhoto(photo);
    }

    @Override
    public void loadCollection(Collection collection) {
        mView.showCollectionDetails(collection);
    }

    @Override
    public void loadPhotos(int page, int limit) {

        mView.showLoading();
        mCollectionService.getSingleCollection(collection.getId(), page, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Photo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Photo> photos) {
                        mView.refreshPhotos(photos);
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        mView.showError(e.getMessage());
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public void loadMorePhotos(int page, int limit) {

        mView.showLoading();
        mCollectionService.getSingleCollection(collection.getId(), page, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Photo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Photo> photos) {
                        mView.addPhotos(photos);
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        mView.showError(e.getMessage());
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
