package com.codemybrainsout.imageviewer.view.user

import com.codemybrainsout.imageviewer.view.base.BasePresenter
import com.codemybrainsout.imageviewer.view.base.BaseView
import com.codemybrainsout.imageviewer.model.*

/**
 * Created by ahulr on 10-06-2017.
 */
class UserContract {
     interface Presenter : BasePresenter {
        fun loadUser(username: String?)
        fun loadPhotos(username: String?, page: Int, limit: Int)
        fun loadMorePhotos(username: String?, page: Int, limit: Int)
        fun openPhotoDetails(photo: Photo?)
    }

    interface View : BaseView<Presenter?> {
        fun showUser(user: User)
        fun refreshPhotos(list: List<Photo>)
        fun addPhotos(list: List<Photo>)
        fun showPhotoDetails(photo: Photo?)
    }
}