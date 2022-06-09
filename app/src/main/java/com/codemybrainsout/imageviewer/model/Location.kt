package com.codemybrainsout.imageviewer.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by ahulr on 08-06-2017.
 */
@Parcelize
data class Location(
    @SerializedName("city")
    @Expose
    var city: String? = null,

    @SerializedName("country")
    @Expose
    var country: String? = null
) : Parcelable