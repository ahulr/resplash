package com.codemybrainsout.imageviewer.view.detail

import com.codemybrainsout.imageviewer.api.PhotoService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by ahulr on 09-06-2017.
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class DetailModule {

    @Binds
    abstract fun bindActivity(activity: DetailActivity): DetailContract.View

    @Binds
    abstract fun bindPresenter(impl: DetailPresenter): DetailContract.Presenter
}