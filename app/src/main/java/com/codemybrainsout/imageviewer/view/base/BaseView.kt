package com.codemybrainsout.imageviewer.view.base

/**
 * Created by ahulr on 09-06-2017.
 */
interface BaseView<T> {
    fun showLoading()
    fun hideLoading()
    fun showError(s: String)
}