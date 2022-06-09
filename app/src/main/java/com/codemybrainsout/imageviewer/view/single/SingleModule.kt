package com.codemybrainsout.imageviewer.view.single

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by ahulr on 11-06-2017.
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class SingleModule {

    @Binds
    abstract fun bindActivity(activity: SingleCollectionActivity): SingleContract.View

    @Binds
    abstract fun bindPresenter(impl: SinglePresenter): SingleContract.Presenter

}