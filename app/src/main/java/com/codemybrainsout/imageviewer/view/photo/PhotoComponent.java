package com.codemybrainsout.imageviewer.view.photo;

import com.codemybrainsout.imageviewer.dagger.component.AppComponent;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;

import dagger.Component;
import dagger.Module;

/**
 * Created by ahulr on 10-06-2017.
 */
@ActivityScope
@Component(modules = PhotoModule.class, dependencies = AppComponent.class)
public interface PhotoComponent {

    void inject(PhotoActivity photoActivity);

}
