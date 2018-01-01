package com.codemybrainsout.imageviewer.view.detail;

import com.codemybrainsout.imageviewer.dagger.component.AppComponent;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;

import dagger.Component;

/**
 * Created by ahulr on 09-06-2017.
 */
@ActivityScope
@Component(modules = DetailModule.class, dependencies = AppComponent.class)
public interface DetailComponent {

    void inject(DetailActivity detailActivity);

}
