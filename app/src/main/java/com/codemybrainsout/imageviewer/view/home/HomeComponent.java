package com.codemybrainsout.imageviewer.view.home;

import com.codemybrainsout.imageviewer.dagger.component.AppComponent;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;

import dagger.Component;

/**
 * Created by ahulr on 10-06-2017.
 */
@ActivityScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {

    void inject(HomeActivity homeActivity);

}
