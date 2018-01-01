package com.codemybrainsout.imageviewer.view.photo;

import android.content.Context;

import com.codemybrainsout.imageviewer.api.PhotoService;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ahulr on 10-06-2017.
 */

@Module
public class PhotoModule {

    private Context context;

    public PhotoModule(Context context) {
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

    @ActivityScope
    @Provides
    PhotoPresenter photoPresenter(PhotoService photoService) {
        return new PhotoPresenter(photoService, (PhotoContract.View) context);
    }

}
