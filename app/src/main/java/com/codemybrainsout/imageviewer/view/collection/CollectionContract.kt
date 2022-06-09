package com.codemybrainsout.imageviewer.view.collection

import com.codemybrainsout.imageviewer.view.base.BasePresenter
import com.codemybrainsout.imageviewer.view.base.BaseView
import com.codemybrainsout.imageviewer.model.*
import com.codemybrainsout.imageviewer.model.Collection

/**
 * Created by ahulr on 09-06-2017.
 */
class CollectionContract {
    interface Presenter : BasePresenter {
        fun openUser(user: User?)
        fun loadCollections(page: Int, limit: Int)
        fun loadMoreCollections(page: Int, limit: Int)
        fun openCollection(collection: Collection?)
    }

    interface View : BaseView<Presenter?> {
        fun showUser(user: User?)
        fun showCollection(collection: Collection?)
        fun refreshCollections(list: List<Collection>)
        fun addCollections(list: List<Collection>)
    }
}