package com.codemybrainsout.imageviewer.api;

import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ahulr on 07-06-2017.
 */

public interface UserService {

    @GET("/users/{username}")
    Observable<User> getUser(@Path("username") String user);

    @GET("/users/{username}/photos")
    Observable<List<Photo>> getUserPhotos(@Path("username") String user, @Query("page") int page, @Query("per_page") int limit);

    @GET("/users/{username}/likes")
    Observable<List<Photo>> getUserLikes(@Path("username") String user, @Query("page") int page, @Query("per_page") int limit);

    @GET("/users/{username}/collections")
    Observable<List<Collection>> getUserCollections(@Path("username") String user, @Query("page") int page, @Query("per_page") int limit);

}
