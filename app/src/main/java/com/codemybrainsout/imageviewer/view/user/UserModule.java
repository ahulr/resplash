package com.codemybrainsout.imageviewer.view.user;

import android.content.Context;

import com.codemybrainsout.imageviewer.api.PhotoService;
import com.codemybrainsout.imageviewer.api.UserService;
import com.codemybrainsout.imageviewer.dagger.scope.ActivityScope;
import com.codemybrainsout.imageviewer.view.home.MultiViewAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ahulr on 10-06-2017.
 */

@Module
public class UserModule {

    private Context context;

    public UserModule(Context context) {
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
    UserPresenter userPresenter(UserService userService) {
        return new UserPresenter(userService, (UserContract.View) context);
    }
}
