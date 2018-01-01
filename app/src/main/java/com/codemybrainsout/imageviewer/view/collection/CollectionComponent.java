package com.codemybrainsout.imageviewer.view.collection;

import com.codemybrainsout.imageviewer.dagger.component.AppComponent;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;

import dagger.Component;

/**
 * Created by ahulr on 10-06-2017.
 */

@ActivityScope
@Component(modules = CollectionModule.class, dependencies = AppComponent.class)
public interface CollectionComponent {

    void inject(CollectionActivity collectionActivity);

}
