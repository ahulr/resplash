package com.codemybrainsout.imageviewer.view.user;

import com.codemybrainsout.imageviewer.dagger.component.AppComponent;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;
import com.codemybrainsout.imageviewer.dagger.scope.ApplicationScope;

import dagger.Component;

/**
 * Created by ahulr on 10-06-2017.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = UserModule.class)
public interface UserComponent {

    void inject(UserActivity userActivity);

}
