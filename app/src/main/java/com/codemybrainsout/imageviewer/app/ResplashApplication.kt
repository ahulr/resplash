package com.codemybrainsout.imageviewer.app

import dagger.hilt.android.HiltAndroidApp
import android.app.Application
import timber.log.Timber
import timber.log.Timber.Forest.plant

/**
 * Created by ahulr on 06-06-2017.
 */

@HiltAndroidApp
class ResplashApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        plant(Timber.DebugTree())
    }
}