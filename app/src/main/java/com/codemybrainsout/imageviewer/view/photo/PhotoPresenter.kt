package com.codemybrainsout.imageviewer.view.photo

import com.codemybrainsout.imageviewer.api.PhotoService
import com.codemybrainsout.imageviewer.model.Photo
import com.codemybrainsout.imageviewer.model.User
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by ahulr on 09-06-2017.
 */
class PhotoPresenter @Inject constructor(
    private val photoService: PhotoService,
    private val mView: PhotoContract.View
) : PhotoContract.Presenter {

    override fun openUser(user: User?) {
        mView.showUser(user)
    }

    override fun openPhoto(photo: Photo?) {
        mView.showPhoto(photo)
    }

    override fun loadPhotos(page: Int, limit: Int, orderBy: String?) {
        mView.showLoading()
        photoService.getPhotos(page, limit, orderBy)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Photo>> {
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    mView.showError(e.message)
                    mView.hideLoading()
                }

                override fun onComplete() {}
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(photos: List<Photo>) {
                    mView.refreshPhotos(photos)
                    mView.hideLoading()
                }
            })
    }

    override fun loadMorePhotos(page: Int, limit: Int, orderBy: String?) {
        photoService.getPhotos(page, limit, orderBy)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Photo>> {
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    mView.showError(e.message)
                }

                override fun onComplete() {}
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(photos: List<Photo>) {
                    mView.addPhotos(photos)
                }
            })
    }
}