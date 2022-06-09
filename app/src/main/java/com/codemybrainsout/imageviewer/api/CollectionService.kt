package com.codemybrainsout.imageviewer.api

import com.codemybrainsout.imageviewer.model.Collection
import com.codemybrainsout.imageviewer.model.Photo
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by ahulr on 07-06-2017.
 */
interface CollectionService {

    @GET("/collections")
    fun getCollections(
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): Observable<List<Collection>>

    @GET("/collections/featured")
    fun getFeaturedCollections(
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): Observable<List<Collection>>

    @GET("/collections/curated")
    fun getCuratedCollections(
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): Observable<List<Collection>>

    @GET("/collections/{id}/related")
    fun getRelatedCollections(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): Observable<List<Collection>>

    @GET("collections/{id}/photos")
    fun getSingleCollection(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): Observable<List<Photo>>

    @GET("collections/curated/{id}/photos")
    fun getSingleCuratedCollection(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): Observable<List<Photo>>
}