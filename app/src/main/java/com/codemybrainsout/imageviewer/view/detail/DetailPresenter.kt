package com.codemybrainsout.imageviewer.view.detail

import android.content.Context
import android.content.Intent
import com.codemybrainsout.imageviewer.api.PhotoService
import com.codemybrainsout.imageviewer.model.Exif
import com.codemybrainsout.imageviewer.model.Photo
import com.codemybrainsout.imageviewer.model.User
import com.codemybrainsout.imageviewer.service.FileDownloadService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by ahulr on 09-06-2017.
 */
class DetailPresenter @Inject constructor(
    private val photoService: PhotoService,
    private val view: DetailContract.View,
) : DetailContract.Presenter {

    override fun loadPhoto(photoId: String) {
        view.showLoading()
        photoService.getSinglePhoto(photoId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.showPhoto(it)
                view.hideLoading()
            }, {
                it.printStackTrace()
                view.showError(it.message)
                view.hideLoading()
            })
    }

    override fun downloadPhoto(context: Context, url: String?) {
        val intent: Intent = FileDownloadService.getIntent(context, url, false)
        context.startService(intent)
    }

    override fun setAsWallpaper(context: Context, url: String?) {
        val intent: Intent = FileDownloadService.getIntent(context, url, true)
        context.startService(intent)
    }

    override fun showExif(exif: Exif?) {
        view.showExif(exif)
    }

    override fun openUserDetails(user: User?) {
        view.showUserDetails(user)
    }
}