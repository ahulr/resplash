package com.codemybrainsout.imageviewer.dagger.module

import android.content.Context
import com.codemybrainsout.imageviewer.BuildConfig
import com.codemybrainsout.imageviewer.dagger.scope.ApplicationScope
import com.codemybrainsout.imageviewer.utility.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by ahulr on 09-06-2017.
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @ApplicationScope
    @Provides
    fun file(@ApplicationContext context: Context): File {
        return File(context.cacheDir, "cache")
    }

    @ApplicationScope
    @Provides
    fun cache(file: File): Cache {
        return Cache(file, 10 * 1024 * 1024)
    }

    @ApplicationScope
    @Provides
    fun interceptor(@ApplicationContext context: Context): Interceptor {
        return Interceptor { chain ->
            val original: Request = chain.request()
            val builder: Request.Builder = if (NetworkUtils.hasNetwork(context)) {
            val maxAge = 60 // read from cache for 1 minute
            original.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept-Version", BuildConfig.API_VERSION)
                .addHeader(
                    "Authorization",
                    "Client-ID " + BuildConfig.UNSPLASH_APPLICATION_ID
                )
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=$maxAge")
        } else {
            val maxStale = 60 * 60 * 24 * 7 // tolerate 7 days stale
            original.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept-Version", BuildConfig.API_VERSION)
                .addHeader(
                    "Authorization",
                    "Client-ID " + BuildConfig.UNSPLASH_APPLICATION_ID
                )
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
        }
            val request = builder.build()
            chain.proceed(request)
        }
    }

    @ApplicationScope
    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @ApplicationScope
    @Provides
    fun okHttpClient(
        interceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        cache: Cache?
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(BuildConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(BuildConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
            .cache(cache)
            .build()
    }

    @ApplicationScope
    @Provides
    fun retrofit(okHttpClient: OkHttpClient?): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}