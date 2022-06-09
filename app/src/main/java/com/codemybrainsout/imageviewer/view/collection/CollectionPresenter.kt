package com.codemybrainsout.imageviewer.view.collection

import com.codemybrainsout.imageviewer.api.CollectionService
import com.codemybrainsout.imageviewer.model.Collection
import com.codemybrainsout.imageviewer.model.User
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by ahulr on 09-06-2017.
 */
class CollectionPresenter @Inject constructor(
    private val collectionService: CollectionService,
    private val mView: CollectionContract.View
) : CollectionContract.Presenter {

    override fun openUser(user: User?) {
        mView.showUser(user)
    }

    override fun loadCollections(page: Int, limit: Int) {
        mView.showLoading()
        collectionService.getFeaturedCollections(page, limit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Collection>> {
                override fun onSubscribe(@NonNull d: Disposable?) {}
                override fun onNext(@NonNull collections: List<Collection>) {
                    mView.refreshCollections(collections)
                    mView.hideLoading()
                }

                override fun onError(@NonNull e: Throwable) {
                    e.printStackTrace()
                    mView.showError(e.message)
                    mView.hideLoading()
                }

                override fun onComplete() {}
            })
    }

    override fun loadMoreCollections(page: Int, limit: Int) {
        collectionService.getFeaturedCollections(page, limit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Collection>> {
                override fun onSubscribe(@NonNull d: Disposable?) {}
                override fun onNext(@NonNull collections: List<Collection>) {
                    mView.addCollections(collections)
                }

                override fun onError(@NonNull e: Throwable) {
                    e.printStackTrace()
                    mView.showError(e.message)
                }

                override fun onComplete() {}
            })
    }

    override fun openCollection(collection: Collection?) {
        mView.showCollection(collection)
    }
}