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
            .subscribe({
                mView.refreshPhotos(it)
                mView.hideLoading()
            }, {
                it.printStackTrace()
                mView.showError(it.message)
                mView.hideLoading()
            })
    }

    override fun loadMorePhotos(page: Int, limit: Int, orderBy: String?) {
        photoService.getPhotos(page, limit, orderBy)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mView.addPhotos(it)
            }, {
                it.printStackTrace()
                mView.showError(it.message)
            })

    }
}