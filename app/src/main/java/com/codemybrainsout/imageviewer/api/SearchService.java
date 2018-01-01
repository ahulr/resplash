package com.codemybrainsout.imageviewer.api;

import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.Search;
import com.codemybrainsout.imageviewer.model.User;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ahulr on 07-06-2017.
 */

public interface SearchService {

    @GET("/search/photos")
    Observable<Search> searchPhoto(@Query("query") String query, @Query("page") int page, @Query("per_page") int limit);
}
