package com.codemybrainsout.imageviewer.dagger.module;

import android.content.Context;

import com.codemybrainsout.imageviewer.dagger.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ahulr on 09-06-2017.
 */

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @ApplicationScope
    @Provides
    public Context context() {
        return context;
    }
}
