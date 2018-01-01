package com.codemybrainsout.imageviewer.view.photo;

import com.codemybrainsout.imageviewer.api.PhotoService;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ahulr on 09-06-2017.
 */

public class PhotoPresenter implements PhotoContract.Presenter {

    private final PhotoContract.View mView;
    private final PhotoService mPhotoService;

    public PhotoPresenter(PhotoService photoService, PhotoContract.View view) {
        mView = view;
        mPhotoService = photoService;
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
    public void loadPhotos(int page, int limit, String orderBy) {

        mView.showLoading();
        mPhotoService.getPhotos(page, limit, orderBy)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Photo>>() {

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
                    public void onNext(List<Photo> photos) {
                        mView.refreshPhotos(photos);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void loadMorePhotos(int page, int limit, String orderBy) {
        mPhotoService.getPhotos(page, limit, orderBy)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Photo>>() {

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
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
                        mView.addPhotos(photos);
                    }
                });
    }
}
