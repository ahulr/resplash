package com.codemybrainsout.imageviewer.app;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.dagger.component.AppComponent;
import com.codemybrainsout.imageviewer.dagger.component.DaggerAppComponent;
import com.codemybrainsout.imageviewer.dagger.module.ContextModule;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by ahulr on 06-06-2017.
 */

public class ResplashApplication extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initCalligraphy();
        Timber.plant(new Timber.DebugTree());
        component = DaggerAppComponent.builder().contextModule(new ContextModule(this)).build();
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public AppComponent getComponent() {
        return component;
    }
}
