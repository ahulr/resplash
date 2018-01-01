package com.codemybrainsout.imageviewer.view.detail;

import android.content.Context;

import com.codemybrainsout.imageviewer.api.PhotoService;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;
import com.codemybrainsout.imageviewer.dagger.scope.ApplicationScope;
import com.codemybrainsout.imageviewer.model.Photo;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ahulr on 09-06-2017.
 */

@Module
public class DetailModule {

    private Context context;

    public DetailModule(Context context) {
        this.context = context;
    }

    @ActivityScope
    @Provides
    Context context() {
        return context;
    }

}
