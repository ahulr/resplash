package com.codemybrainsout.imageviewer.view.single

import com.codemybrainsout.imageviewer.view.base.BasePresenter
import com.codemybrainsout.imageviewer.view.base.BaseView
import com.codemybrainsout.imageviewer.model.*
import com.codemybrainsout.imageviewer.model.Collection

/**
 * Created by ahulr on 11-06-2017.
 */
class SingleContract {
    interface Presenter : BasePresenter {
        fun openUser(user: User?)
        fun openPhoto(photo: Photo?)
        fun loadCollection(collection: Collection?)
        fun loadPhotos(collectionId: Int, page: Int, limit: Int)
        fun loadMorePhotos(collectionId: Int, page: Int, limit: Int)
    }

    interface View : BaseView<Presenter?> {
        fun showUser(user: User?)
        fun showPhoto(photo: Photo?)
        fun showCollectionDetails(collection: Collection?)
        fun refreshPhotos(list: List<Photo>)
        fun addPhotos(list: List<Photo>)
    }
}