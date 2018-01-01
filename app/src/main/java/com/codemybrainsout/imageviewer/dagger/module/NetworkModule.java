package com.codemybrainsout.imageviewer.dagger.module;

import android.content.Context;

import com.codemybrainsout.imageviewer.BuildConfig;
import com.codemybrainsout.imageviewer.dagger.scope.ApplicationScope;
import com.codemybrainsout.imageviewer.utility.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ahulr on 09-06-2017.
 */

@Module(includes = ContextModule.class)
public class NetworkModule {

    @ApplicationScope
    @Provides
    public File file(Context context) {
        return new File(context.getCacheDir(), "cache");
    }

    @ApplicationScope
    @Provides
    public Cache cache(File file) {
        return new Cache(file, 10 * 1024 * 1024);
    }

    @ApplicationScope
    @Provides
    public Interceptor interceptor(final Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder builder;
                if (NetworkUtils.hasNetwork(context)) {
                    int maxAge = 60; // read from cache for 1 minute

                    builder = original.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept-Version", BuildConfig.API_VERSION)
                            .addHeader("Authorization", "Client-ID " + BuildConfig.UNSPLASH_APPLICATION_ID)
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, max-age=" + maxAge);
                } else {
                    int maxStale = 60 * 60 * 24 * 7; // tolerate 7 days stale
                    builder = original.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept-Version", BuildConfig.API_VERSION)
                            .addHeader("Authorization", "Client-ID " + BuildConfig.UNSPLASH_APPLICATION_ID)
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
                }

                Request request = builder.build();
                return chain.proceed(request);
            }
        };
    }

    @ApplicationScope
    @Provides
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @ApplicationScope
    @Provides
    public OkHttpClient okHttpClient(Interceptor interceptor, HttpLoggingInterceptor httpLoggingInterceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .readTimeout(BuildConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(BuildConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .addInterceptor(httpLoggingInterceptor)
                .cache(cache)
                .build();
    }

}
