package com.codemybrainsout.imageviewer.view.home;

import android.content.Context;

import com.codemybrainsout.imageviewer.api.CollectionService;
import com.codemybrainsout.imageviewer.api.PhotoService;
import com.codemybrainsout.imageviewer.dagger.module.ContextModule;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;
import com.codemybrainsout.imageviewer.dagger.scope.ApplicationScope;
import com.codemybrainsout.imageviewer.model.Collection;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ahulr on 10-06-2017.
 */
@Module()
public class HomeModule {

    private Context context;

    public HomeModule(Context context) {
        this.context = context;
    }

    @ActivityScope
    @Provides
    Context context() {
        return context;
    }

    @ActivityScope
    @Provides
    MultiViewAdapter multiViewAdapter(Context context) {
        return new MultiViewAdapter(context);
    }

    @ActivityScope
    @Provides
    HomePresenter homePresenter(PhotoService photoService, CollectionService collectionService) {
        return new HomePresenter(photoService, collectionService, (HomeContract.View) context);
    }

}
