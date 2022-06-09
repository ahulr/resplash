package com.codemybrainsout.imageviewer.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by ahulr on 06-06-2017.
 */
@Parcelize
data class Photo(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("width")
    @Expose
    var width: Int? = null,

    @SerializedName("height")
    @Expose
    var height: Int? = null,

    @SerializedName("color")
    @Expose
    var color: String? = null,

    @SerializedName("downloads")
    @Expose
    var downloads: Int? = null,

    @SerializedName("likes")
    @Expose
    var likes: Int? = null,

    @SerializedName("liked_by_user")
    @Expose
    var likedByUser: Boolean? = null,

    @SerializedName("exif")
    @Expose
    var exif: Exif? = null,

    @SerializedName("location")
    @Expose
    var location: Location? = null,

    @SerializedName("urls")
    @Expose
    var urls: Urls? = null,

    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null,

    @SerializedName("user")
    @Expose
    var user: User? = null
) : BaseModel(), Parcelable {

}