package com.codemybrainsout.imageviewer.view.collection

import com.codemybrainsout.imageviewer.api.CollectionService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by ahulr on 10-06-2017.
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class CollectionModule {

    @Provides
    abstract fun bindService(): CollectionService

    @Binds
    abstract fun bindActivity(activity: CollectionActivity): CollectionContract.View

    @Binds
    abstract fun bindPresenter(impl: CollectionPresenter): CollectionContract.Presenter

}