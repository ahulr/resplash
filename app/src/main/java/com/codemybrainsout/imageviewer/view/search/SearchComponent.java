package com.codemybrainsout.imageviewer.view.search;

import com.codemybrainsout.imageviewer.dagger.component.AppComponent;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;
import com.codemybrainsout.imageviewer.dagger.scope.ApplicationScope;

import dagger.Component;

/**
 * Created by ahulr on 11-06-2017.
 */

@ActivityScope
@Component(modules = SearchModule.class, dependencies = AppComponent.class)
public interface SearchComponent {
    void inject(SearchActivity searchActivity);
}
