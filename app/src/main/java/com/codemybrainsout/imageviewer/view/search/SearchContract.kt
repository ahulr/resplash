package com.codemybrainsout.imageviewer.view.search

import com.codemybrainsout.imageviewer.view.base.BasePresenter
import com.codemybrainsout.imageviewer.view.base.BaseView
import com.codemybrainsout.imageviewer.model.*

/**
 * Created by ahulr on 11-06-2017.
 */
class SearchContract {
    interface Presenter : BasePresenter {
        fun openUser(user: User?)
        fun openPhoto(photo: Photo?)
        fun searchPhotos(query: String?, page: Int, limit: Int)
        fun searchMorePhotos(query: String?, page: Int, limit: Int)
    }

    interface View : BaseView<Presenter?> {
        fun showUser(user: User?)
        fun showPhoto(photo: Photo?)
        fun refreshPhotos(list: List<Photo>)
        fun addPhotos(list: List<Photo>)
    }
}