package com.codemybrainsout.imageviewer.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("username")
    @Expose
    var username: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null,

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null,

    @SerializedName("portfolio_url")
    @Expose
    var portfolioUrl: Urls? = null,

    @SerializedName("bio")
    @Expose
    var bio: String? = null,

    @SerializedName("location")
    @Expose
    var location: String? = null,

    @SerializedName("total_likes")
    @Expose
    var totalLikes: Int? = null,

    @SerializedName("total_photos")
    @Expose
    var totalPhotos: Int? = null,

    @SerializedName("total_collections")
    @Expose
    var totalCollections: Int? = null,

    @SerializedName("followed_by_user")
    @Expose
    var followedByUser: Boolean? = null,

    @SerializedName("followers_count")
    @Expose
    var followersCount: Int? = null,

    @SerializedName("following_count")
    @Expose
    var followingCount: Int? = null,

    @SerializedName("downloads")
    @Expose
    var downloads: Int? = null,

    @SerializedName("profile_image")
    @Expose
    var profileImage: ProfileImage? = null
) : Parcelable
