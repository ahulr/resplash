package com.codemybrainsout.imageviewer.dagger.component;

import android.content.Context;

import com.codemybrainsout.imageviewer.api.CollectionService;
import com.codemybrainsout.imageviewer.api.PhotoService;
import com.codemybrainsout.imageviewer.api.SearchService;
import com.codemybrainsout.imageviewer.api.UserService;
import com.codemybrainsout.imageviewer.dagger.module.ServiceModule;
import com.codemybrainsout.imageviewer.dagger.scope.ApplicationScope;

import dagger.Component;

/**
 * Created by ahulr on 09-06-2017.
 */
@ApplicationScope
@Component(modules = {ServiceModule.class})
public interface AppComponent {

    PhotoService photoService();

    CollectionService collectionService();

    UserService userService();

    SearchService searchService();
}
