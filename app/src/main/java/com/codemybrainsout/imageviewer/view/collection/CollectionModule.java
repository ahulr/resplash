package com.codemybrainsout.imageviewer.view.collection;

import android.content.Context;

import com.codemybrainsout.imageviewer.api.CollectionService;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ahulr on 10-06-2017.
 */

@Module
public class CollectionModule {

    private Context context;

    public CollectionModule(Context context) {
        this.context = context;
    }

    @ActivityScope
    @Provides
    Context context() {
        return context;
    }

    @ActivityScope
    @Provides
    CollectionAdapter collectionAdapter(Context context) {
        return new CollectionAdapter(context);
    }

    @ActivityScope
    @Provides
    CollectionPresenter collectionPresenter(CollectionService collectionService) {
        return new CollectionPresenter(collectionService, (CollectionContract.View) context);
    }

}
