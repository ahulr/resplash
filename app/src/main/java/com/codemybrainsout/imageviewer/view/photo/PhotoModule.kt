package com.codemybrainsout.imageviewer.view.photo

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by ahulr on 10-06-2017.
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class PhotoModule {

    @Binds
    abstract fun bindActivity(activity: PhotoActivity): PhotoContract.View

    @Binds
    abstract fun bindPresenter(impl: PhotoPresenter): PhotoContract.Presenter
}