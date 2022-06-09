package com.codemybrainsout.imageviewer.api

import retrofit2.http.GET
import com.codemybrainsout.imageviewer.model.Search
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Query

/**
 * Created by ahulr on 07-06-2017.
 */
interface SearchService {
    @GET("/search/photos")
    fun searchPhoto(
        @Query("query") query: String?,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): Observable<Search>
}