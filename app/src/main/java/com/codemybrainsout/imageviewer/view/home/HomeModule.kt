package com.codemybrainsout.imageviewer.view.home

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by ahulr on 10-06-2017.
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class HomeModule {

    @Binds
    abstract fun bindActivity(activity: HomeActivity): HomeContract.View

    @Binds
    abstract fun bindPresenter(impl: HomePresenter): HomeContract.Presenter

}