package com.codemybrainsout.imageviewer.dagger.module

import com.codemybrainsout.imageviewer.BuildConfig
import com.codemybrainsout.imageviewer.api.CollectionService
import com.codemybrainsout.imageviewer.api.PhotoService
import com.codemybrainsout.imageviewer.api.SearchService
import com.codemybrainsout.imageviewer.api.UserService
import com.codemybrainsout.imageviewer.dagger.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by ahulr on 09-06-2017.
 */

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun photoService(retrofit: Retrofit): PhotoService {
        return retrofit.create(PhotoService::class.java)
    }

    @Provides
    fun collectionService(retrofit: Retrofit): CollectionService {
        return retrofit.create(CollectionService::class.java)
    }

    @Provides
    fun userService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    fun searchService(retrofit: Retrofit): SearchService {
        return retrofit.create(SearchService::class.java)
    }
}