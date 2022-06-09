package com.codemybrainsout.imageviewer.view.detail

import com.codemybrainsout.imageviewer.view.base.BasePresenter
import com.codemybrainsout.imageviewer.view.base.BaseView
import android.content.Context
import com.codemybrainsout.imageviewer.model.*

/**
 * Created by ahulr on 09-06-2017.
 */
class DetailContract {

    interface Presenter : BasePresenter {
        fun loadPhoto(photoId: String)
        fun downloadPhoto(context: Context, url: String?)
        fun setAsWallpaper(context: Context, url: String?)
        fun showExif(exif: Exif?)
        fun openUserDetails(user: User?)
    }

    interface View : BaseView<Presenter?> {
        fun showPhoto(photo: Photo)
        fun showExif(exif: Exif?)
        fun showUserDetails(user: User?)
    }
}