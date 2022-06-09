package com.codemybrainsout.imageviewer.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by ahulr on 08-06-2017.
 */
@Parcelize
data class Exif(
    @SerializedName("make")
    @Expose
    var make: String? = null,

    @SerializedName("model")
    @Expose
    var model: String? = null,

    @SerializedName("exposure_time")
    @Expose
    var exposureTime: String? = null,

    @SerializedName("aperture")
    @Expose
    var aperture: String? = null,

    @SerializedName("focal_length")
    @Expose
    var focalLength: String? = null,

    @SerializedName("iso")
    @Expose
    var iso: Int? = null,
    var downloads: Int? = null,
    var likes: Int? = null
) : Parcelable