package com.codemybrainsout.imageviewer.api;

import com.codemybrainsout.imageviewer.model.Photo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ahulr on 07-06-2017.
 */

public interface PhotoService {

    @GET("/photos")
    Observable<List<Photo>> getPhotos(@Query("page") int page, @Query("per_page") int limit, @Query("order_by") String orderBy); //orderBy: latest, oldest, popular

    @GET("/photos/curated")
    Observable<List<Photo>> getCuratedPhotos(@Query("page") int page, @Query("per_page") int limit, @Query("order_by") String orderBy); //orderBy: latest, oldest, popular

    @GET("/photos/{id}")
    Observable<Photo> getSinglePhoto(@Path("id") String id);

}
