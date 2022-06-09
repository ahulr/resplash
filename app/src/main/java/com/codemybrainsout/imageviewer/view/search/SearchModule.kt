package com.codemybrainsout.imageviewer.view.search

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by ahulr on 11-06-2017.
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class SearchModule {

    @Binds
    abstract fun bindActivity(activity: SearchActivity): SearchContract.View

    @Binds
    abstract fun bindPresenter(impl: SearchPresenter): SearchContract.Presenter

}