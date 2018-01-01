package com.codemybrainsout.imageviewer.dagger.module;

import com.codemybrainsout.imageviewer.BuildConfig;
import com.codemybrainsout.imageviewer.api.CollectionService;
import com.codemybrainsout.imageviewer.api.PhotoService;
import com.codemybrainsout.imageviewer.api.SearchService;
import com.codemybrainsout.imageviewer.api.UserService;
import com.codemybrainsout.imageviewer.dagger.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ahulr on 09-06-2017.
 */
@Module(includes = NetworkModule.class)
public class ServiceModule {

    @ApplicationScope
    @Provides
    public Retrofit retrofit(OkHttpClient httpClient) {
        return provideRetrofit(httpClient);
    }

    public static Retrofit provideRetrofit(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.ENDPOINT)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @ApplicationScope
    @Provides
    public PhotoService photoService(Retrofit retrofit) {
        return retrofit.create(PhotoService.class);
    }

    @ApplicationScope
    @Provides
    public CollectionService collectionService(Retrofit retrofit) {
        return retrofit.create(CollectionService.class);
    }

    @ApplicationScope
    @Provides
    public UserService userService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @ApplicationScope
    @Provides
    public SearchService searchService(Retrofit retrofit) {
        return retrofit.create(SearchService.class);
    }
}
