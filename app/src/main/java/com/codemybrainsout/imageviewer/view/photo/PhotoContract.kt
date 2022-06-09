package com.codemybrainsout.imageviewer.view.photo

import com.codemybrainsout.imageviewer.view.base.BasePresenter
import com.codemybrainsout.imageviewer.view.base.BaseView
import com.codemybrainsout.imageviewer.model.*

/**
 * Created by ahulr on 09-06-2017.
 */
class PhotoContract {
    interface Presenter : BasePresenter {
        fun openUser(user: User?)
        fun openPhoto(photo: Photo?)
        fun loadPhotos(page: Int, limit: Int, orderBy: String?)
        fun loadMorePhotos(page: Int, limit: Int, orderBy: String?)
    }

    interface View : BaseView<Presenter?> {
        fun showUser(user: User?)
        fun showPhoto(photo: Photo?)
        fun refreshPhotos(list: List<Photo>)
        fun addPhotos(list: List<Photo>)
    }
}