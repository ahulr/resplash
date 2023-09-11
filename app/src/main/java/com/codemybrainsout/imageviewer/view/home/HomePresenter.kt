package com.codemybrainsout.imageviewer.view.home

import com.codemybrainsout.imageviewer.api.CollectionService
import com.codemybrainsout.imageviewer.api.PhotoService
import com.codemybrainsout.imageviewer.model.Collection
import com.codemybrainsout.imageviewer.model.Photo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by ahulr on 10-06-2017.
 */
class HomePresenter @Inject constructor(
    private val photoService: PhotoService,
    private val collectionService: CollectionService,
    val view: HomeContract.View
) : HomeContract.Presenter {

    var page = 1

    override fun loadPhotos(limit: Int, orderBy: String?) {
        view.showLoading()
        photoService.getPhotos(page, limit, orderBy)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.hideLoading()
                view.setPhotos(it)
            }, {
                it.printStackTrace()
                view.hideLoading()
                view.showError(it.message)
            })
    }

    override fun loadCollections(limit: Int) {
        collectionService.getFeaturedCollections(page, limit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.setCollections(it)
            }, {
                it.printStackTrace()
                view.showError(it.message)
            })
    }

    override fun viewAllPhotos() {
        view.openAllPhotos()
    }

    override fun viewAllCollections() {
        view.openAllCollections()
    }

    override fun openPhoto(photo: Photo?) {
        view.showPhoto(photo)
    }

    override fun openCollection(collection: Collection?) {
        view.showCollection(collection)
    }
}