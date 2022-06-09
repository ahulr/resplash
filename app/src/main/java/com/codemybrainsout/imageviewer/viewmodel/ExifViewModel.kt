package com.codemybrainsout.imageviewer.viewmodel

import android.text.TextUtils
import androidx.databinding.BaseObservable
import com.codemybrainsout.imageviewer.model.Exif

/**
 *
 * Viewmodel required for binding exif data to layout_exif.xml
 *
 * Created by ahulr on 11-06-2017.
 */
class ExifViewModel(exif: Exif) : BaseObservable() {
    private val exif: Exif
    val make: String?
        get() = if (TextUtils.isEmpty(exif.make)) "NA" else exif.make
    val model: String?
        get() = if (TextUtils.isEmpty(exif.model)) "NA" else exif.model
    val focal: String
        get() = if (TextUtils.isEmpty(exif.focalLength)) "NA" else exif.focalLength + "mm"
    val aperture: String
        get() = if (TextUtils.isEmpty(exif.aperture)) "NA" else "f/" + exif.aperture
    val exposure: String
        get() = if (TextUtils.isEmpty(exif.exposureTime)) "NA" else exif.exposureTime + "s"
    val iso: String
        get() = if (exif.iso == null) "NA" else "ISO " + exif.iso.toString()
    val likes: String
        get() = if (exif.likes == null) "NA" else exif.likes.toString()
    val downloads: String
        get() = if (exif.downloads == null) "NA" else exif.downloads.toString()

    init {
        this.exif = exif
    }
}