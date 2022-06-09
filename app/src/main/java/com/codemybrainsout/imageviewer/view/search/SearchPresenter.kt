package com.codemybrainsout.imageviewer.view.search

import com.codemybrainsout.imageviewer.api.SearchService
import com.codemybrainsout.imageviewer.model.Photo
import com.codemybrainsout.imageviewer.model.Search
import com.codemybrainsout.imageviewer.model.User
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by ahulr on 11-06-2017.
 */
class SearchPresenter @Inject constructor(private val searchService: SearchService, val view: SearchContract.View) :
    SearchContract.Presenter {

    override fun openUser(user: User?) {
        view.showUser(user)
    }

    override fun openPhoto(photo: Photo?) {
        view.showPhoto(photo)
    }

    override fun searchPhotos(query: String?, page: Int, limit: Int) {
        view.showLoading()
        searchService.searchPhoto(query, page, limit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Search> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(search: Search) {
                    val photos: List<Photo>? = search.results
                    photos?.let { view.refreshPhotos(it) }
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

    override fun searchMorePhotos(query: String?, page: Int, limit: Int) {
        searchService.searchPhoto(query, page, limit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Search> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(search: Search) {
                    val photos: List<Photo>? = search.results
                    view.addPhotos(photos)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    view.showError(e.message)
                }

                override fun onComplete() {}
            })
    }
}