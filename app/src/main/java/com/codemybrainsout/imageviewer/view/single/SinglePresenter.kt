package com.codemybrainsout.imageviewer.view.single

import com.codemybrainsout.imageviewer.api.CollectionService
import com.codemybrainsout.imageviewer.model.Collection
import com.codemybrainsout.imageviewer.model.Photo
import com.codemybrainsout.imageviewer.model.User
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by ahulr on 11-06-2017.
 */
class SinglePresenter @Inject constructor(
    private val collectionService: CollectionService,
    val view: SingleContract.View,
) : SingleContract.Presenter {

    override fun openUser(user: User?) {
        view.showUser(user)
    }

    override fun openPhoto(photo: Photo?) {
        view.showPhoto(photo)
    }

    override fun loadCollection(collection: Collection?) {
        view.showCollectionDetails(collection)
    }

    override fun loadPhotos(collectionId: Int, page: Int, limit: Int) {
        view.showLoading()
        collectionService.getSingleCollection(collectionId, page, limit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.refreshPhotos(it)
                view.hideLoading()
            }, {
                it.printStackTrace()
                view.showError(it.message)
                view.hideLoading()
            })
    }

    override fun loadMorePhotos(collectionId: Int, page: Int, limit: Int) {
        view.showLoading()
        collectionService.getSingleCollection(collectionId, page, limit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.addPhotos(it)
                    view.hideLoading()
                },
                {
                    it.printStackTrace()
                    view.showError(it.message)
                    view.hideLoading()
                }
            )
    }
}