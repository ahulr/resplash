package com.codemybrainsout.imageviewer.view.search;

import android.content.Context;

import com.codemybrainsout.imageviewer.api.SearchService;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;
import com.codemybrainsout.imageviewer.view.home.MultiViewAdapter;
import com.codemybrainsout.imageviewer.view.photo.PhotoAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ahulr on 11-06-2017.
 */

@Module
public class SearchModule {

    private Context context;

    public SearchModule(Context context) {
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
    SearchPresenter searchPresenter(SearchService searchService) {
        return new SearchPresenter(searchService, (SearchContract.View) context);
    }
}
