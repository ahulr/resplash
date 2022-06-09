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
            .subscribe(object : Observer<List<Photo>> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(photos: List<Photo>) {
                    view.refreshPhotos(photos)
                    view.hideLoading()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    view.showError(e.message)
                    view.hideLoading()
                }

                override fun onComplete() {}
            })
    }

    override fun loadMorePhotos(collectionId: Int, page: Int, limit: Int) {
        view.showLoading()
        collectionService.getSingleCollection(collectionId, page, limit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Photo>> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(photos: List<Photo>) {
                    view.addPhotos(photos)
                    view.hideLoading()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    view.showError(e.message)
                    view.hideLoading()
                }

                override fun onComplete() {}
            })
    }
}