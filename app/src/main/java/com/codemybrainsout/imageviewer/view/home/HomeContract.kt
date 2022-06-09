package com.codemybrainsout.imageviewer.view.home

import com.codemybrainsout.imageviewer.view.base.BasePresenter
import com.codemybrainsout.imageviewer.view.base.BaseView
import com.codemybrainsout.imageviewer.model.*
import com.codemybrainsout.imageviewer.model.Collection

/**
 * Created by ahulr on 10-06-2017.
 */
class HomeContract {
    interface Presenter : BasePresenter {
        fun loadPhotos(limit: Int, orderBy: String?)
        fun loadCollections(limit: Int)
        fun viewAllPhotos()
        fun viewAllCollections()
        fun openPhoto(photo: Photo?)
        fun openCollection(collection: Collection?)
    }

    interface View : BaseView<Presenter?> {
        fun setPhotos(list: List<Photo>)
        fun setCollections(list: List<Collection>)
        fun openAllCollections()
        fun openAllPhotos()
        override fun showError(s: String?)
        fun showPhoto(photo: Photo?)
        fun showCollection(collection: Collection?)
    }
}