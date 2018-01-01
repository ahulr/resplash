package com.codemybrainsout.imageviewer.view.single;

import android.content.Context;

import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;
import com.codemybrainsout.imageviewer.view.photo.PhotoAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ahulr on 11-06-2017.
 */

@Module
public class SingleModule {

    private Context context;

    public SingleModule(Context context) {
        this.context = context;
    }

    @ActivityScope
    @Provides
    Context context() {
        return context;
    }

    @ActivityScope
    @Provides
    PhotoAdapter photoAdapter(Context context) {
        return new PhotoAdapter(context);
    }

}
