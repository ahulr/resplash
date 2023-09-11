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
            .subscribe({
                mView.refreshCollections(it)
                mView.hideLoading()
            }, {
                it.printStackTrace()
                mView.showError(it.message)
                mView.hideLoading()
            })
    }

    override fun loadMoreCollections(page: Int, limit: Int) {
        collectionService.getFeaturedCollections(page, limit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mView.addCollections(it)
            }, {
                it.printStackTrace()
                mView.showError(it.message)
            })
    }

    override fun openCollection(collection: Collection?) {
        mView.showCollection(collection)
    }
}