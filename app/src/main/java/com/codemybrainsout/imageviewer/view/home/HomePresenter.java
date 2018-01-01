package com.codemybrainsout.imageviewer.view.home;

import com.codemybrainsout.imageviewer.api.CollectionService;
import com.codemybrainsout.imageviewer.api.PhotoService;
import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Photo;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ahulr on 10-06-2017.
 */

public class HomePresenter implements HomeContract.Presenter {

    int page = 1;

    private PhotoService photoService;
    private CollectionService collectionService;
    private HomeContract.View mView;

    public HomePresenter(PhotoService photoService, CollectionService collectionService, HomeContract.View view) {
        this.photoService = photoService;
        this.collectionService = collectionService;
        this.mView = view;
    }

    @Override
    public void loadPhotos(int limit, String orderBy) {
        mView.showLoading();
        photoService.getPhotos(page, limit, orderBy)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Photo>>() {

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoading();
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(List<Photo> photos) {
                        mView.hideLoading();
                        mView.setPhotos(photos);
                    }
                });
    }

    @Override
    public void loadCollections(int limit) {
        collectionService.getFeaturedCollections(page, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Collection>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Collection> collections) {
                        mView.setCollections(collections);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void viewAllPhotos() {
        mView.openAllPhotos();
    }

    @Override
    public void viewAllCollections() {
        mView.openAllCollections();
    }

    @Override
    public void openPhoto(Photo photo) {
        mView.showPhoto(photo);
    }

    @Override
    public void openCollection(Collection collection) {
        mView.showCollection(collection);
    }
}
