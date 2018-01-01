package com.codemybrainsout.imageviewer.api;

import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Photo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ahulr on 07-06-2017.
 */

public interface CollectionService {

    @GET("/collections")
    Observable<List<Collection>> getCollections(@Query("page") int page, @Query("per_page") int limit);

    @GET("/collections/featured")
    Observable<List<Collection>> getFeaturedCollections(@Query("page") int page, @Query("per_page") int limit);

    @GET("/collections/curated")
    Observable<List<Collection>> getCuratedCollections(@Query("page") int page, @Query("per_page") int limit);

    @GET("/collections/{id}/related")
    Observable<List<Collection>> getRelatedCollections(@Path("id") int id, @Query("page") int page, @Query("per_page") int limit);

    @GET("collections/{id}/photos")
    Observable<List<Photo>> getSingleCollection(@Path("id") int id, @Query("page") int page, @Query("per_page") int limit);

    @GET("collections/curated/{id}/photos")
    Observable<List<Photo>> getSingleCuratedCollection(@Path("id") int id, @Query("page") int page, @Query("per_page") int limit);

}
