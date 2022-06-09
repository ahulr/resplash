package com.codemybrainsout.imageviewer.view.user

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by ahulr on 10-06-2017.
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class UserModule {

    @Binds
    abstract fun bindActivity(activity: UserActivity): UserContract.View

    @Binds
    abstract fun bindPresenter(impl: UserPresenter): UserContract.Presenter

}