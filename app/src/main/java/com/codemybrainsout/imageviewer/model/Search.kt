package com.codemybrainsout.imageviewer.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by ahulr on 11-06-2017.
 */
@Parcelize
data class Search(
    @SerializedName("total")
    @Expose
    var total: Int? = null,

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null,

    @SerializedName("results")
    @Expose
    var results: List<Photo>? = null
) : Parcelable