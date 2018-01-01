package com.codemybrainsout.imageviewer.view.base;

/**
 * Created by ahulr on 09-06-2017.
 */

public interface BaseView<T> {

    void showLoading();

    void hideLoading();

    void showError(String s);
}
