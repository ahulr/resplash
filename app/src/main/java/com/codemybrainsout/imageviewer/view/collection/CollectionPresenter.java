package com.codemybrainsout.imageviewer.view.collection;

import com.codemybrainsout.imageviewer.api.CollectionService;
import com.codemybrainsout.imageviewer.model.Collection;
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

public class CollectionPresenter implements CollectionContract.Presenter {

    private final CollectionContract.View mView;
    private CollectionService mCollectionService;

    public CollectionPresenter(CollectionService collectionService, CollectionContract.View view) {
        mView = view;
        mCollectionService = collectionService;
    }

    @Override
    public void openUser(User user) {
        mView.showUser(user);
    }

    @Override
    public void loadCollections(int page, int limit) {

        mView.showLoading();
        mCollectionService.getFeaturedCollections(page, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Collection>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Collection> collections) {
                        mView.refreshCollections(collections);
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
    public void loadMoreCollections(int page, int limit) {
        mCollectionService.getFeaturedCollections(page, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Collection>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Collection> collections) {
                        mView.addCollections(collections);
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
    public void openCollection(Collection collection) {
        mView.showCollection(collection);
    }
}
