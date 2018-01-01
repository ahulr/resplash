package com.codemybrainsout.imageviewer.view.user;

import com.codemybrainsout.imageviewer.api.UserService;
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
 * Created by ahulr on 10-06-2017.
 */

public class UserPresenter implements UserContract.Presenter {

    private UserContract.View mView;
    private UserService mUserService;

    public UserPresenter(UserService userService, UserContract.View view) {
        this.mView = view;
        this.mUserService = userService;
    }

    @Override
    public void loadUser(String username) {

        mView.showLoading();
        mUserService.getUser(username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull User user) {
                        mView.showUser(user);
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
    public void loadPhotos(String username, int page, int limit) {
        mUserService.getUserPhotos(username, page, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Photo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Photo> photos) {
                        mView.refreshPhotos(photos);
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
    public void loadMorePhotos(String username, int page, int limit) {
        mUserService.getUserPhotos(username, page, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Photo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Photo> photos) {
                        mView.addPhotos(photos);
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
    public void openPhotoDetails(Photo photo) {
        mView.showPhotoDetails(photo);
    }
}
