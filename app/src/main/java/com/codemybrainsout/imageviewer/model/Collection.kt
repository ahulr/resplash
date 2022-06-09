package com.codemybrainsout.imageviewer.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by ahulr on 06-06-2017.
 */
@Parcelize
class Collection(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("description")
    @Expose
    var description: String? = null,

    @SerializedName("published_at")
    @Expose
    var publishedAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("curated")
    @Expose
    var curated: Boolean? = null,

    @SerializedName("featured")
    @Expose
    var featured: Boolean? = null,

    @SerializedName("total_photos")
    @Expose
    var totalPhotos: Int? = null,

    @SerializedName("private")
    @Expose
    var private: Boolean? = null,

    @SerializedName("share_key")
    @Expose
    var shareKey: String? = null,

    @SerializedName("cover_photo")
    @Expose
    var coverPhoto: CoverPhoto? = null,

    @SerializedName("user")
    @Expose
    var user: User? = null
) : BaseModel(), Parcelable