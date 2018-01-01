package com.codemybrainsout.imageviewer.view.search;

import com.codemybrainsout.imageviewer.api.SearchService;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.Search;
import com.codemybrainsout.imageviewer.model.User;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ahulr on 11-06-2017.
 */

public class SearchPresenter implements SearchContract.Presenter {

    private SearchService searchService;
    private SearchContract.View view;

    public SearchPresenter(SearchService searchService, SearchContract.View view) {
        this.searchService = searchService;
        this.view = view;
    }

    @Override
    public void openUser(User user) {
        view.showUser(user);
    }

    @Override
    public void openPhoto(Photo photo) {
        view.showPhoto(photo);
    }

    @Override
    public void searchPhotos(String query, int page, int limit) {
        view.showLoading();
        searchService.searchPhoto(query, page, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Search>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Search search) {
                        List<Photo> photos = search.getResults();
                        view.refreshPhotos(photos);
                        view.hideLoading();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        view.showError(e.getMessage());
                        view.hideLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void searchMorePhotos(String query, int page, int limit) {
        searchService.searchPhoto(query, page, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Search>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Search search) {
                        List<Photo> photos = search.getResults();
                        view.addPhotos(photos);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        view.showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
