package com.codemybrainsout.imageviewer.view.single;

import com.codemybrainsout.imageviewer.dagger.component.AppComponent;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;

import dagger.Component;

/**
 * Created by ahulr on 11-06-2017.
 */

@ActivityScope
@Component(modules = SingleModule.class, dependencies = AppComponent.class)
public interface SingleComponent {
    void inject(SingleCollectionActivity singleCollectionActivity);
}
