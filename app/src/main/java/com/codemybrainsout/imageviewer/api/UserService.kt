package com.codemybrainsout.imageviewer.api

import com.codemybrainsout.imageviewer.model.Collection
import com.codemybrainsout.imageviewer.model.Photo
import com.codemybrainsout.imageviewer.model.User
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by ahulr on 07-06-2017.
 */
interface UserService {
    @GET("/users/{username}")
    fun getUser(@Path("username") user: String?): Observable<User>

    @GET("/users/{username}/photos")
    fun getUserPhotos(
        @Path("username") user: String?,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): Observable<List<Photo>>

    @GET("/users/{username}/likes")
    fun getUserLikes(
        @Path("username") user: String?,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): Observable<List<Photo>>

    @GET("/users/{username}/collections")
    fun getUserCollections(
        @Path("username") user: String?,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): Observable<List<Collection>>
}