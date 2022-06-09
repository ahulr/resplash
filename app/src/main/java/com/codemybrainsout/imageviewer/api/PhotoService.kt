package com.codemybrainsout.imageviewer.api

import com.codemybrainsout.imageviewer.model.Photo
import retrofit2.http.GET
import com.codemybrainsout.imageviewer.model.Search
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by ahulr on 07-06-2017.
 */
interface PhotoService {
    @GET("/photos")
    fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") limit: Int,
        @Query("order_by") orderBy: String?
    ): Observable<List<Photo>> //orderBy: latest, oldest, popular

    @GET("/photos/curated")
    fun getCuratedPhotos(
        @Query("page") page: Int,
        @Query("per_page") limit: Int,
        @Query("order_by") orderBy: String?
    ): Observable<List<Photo>> //orderBy: latest, oldest, popular

    @GET("/photos/{id}")
    fun getSinglePhoto(@Path("id") id: String?): Observable<Photo>
}