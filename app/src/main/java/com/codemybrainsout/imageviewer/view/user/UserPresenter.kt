package com.codemybrainsout.imageviewer.view.user

import com.codemybrainsout.imageviewer.api.UserService
import com.codemybrainsout.imageviewer.model.Photo
import com.codemybrainsout.imageviewer.model.User
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by ahulr on 10-06-2017.
 */
class UserPresenter @Inject constructor(
    private val userService: UserService,
    private val mView: UserContract.View
) : UserContract.Presenter {

    override fun loadUser(username: String?) {
        mView.showLoading()
        userService.getUser(username)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<User> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(user: User) {
                    mView.showUser(user)
                    mView.hideLoading()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    mView.showError(e.message)
                    mView.hideLoading()
                }

                override fun onComplete() {}
            })
    }

    override fun loadPhotos(username: String?, page: Int, limit: Int) {
        userService.getUserPhotos(username, page, limit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Photo>> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(photos: List<Photo>) {
                    mView.refreshPhotos(photos)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    mView.showError(e.message)
                }

                override fun onComplete() {}
            })
    }

    override fun loadMorePhotos(username: String?, page: Int, limit: Int) {
        userService.getUserPhotos(username, page, limit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Photo>> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(photos: List<Photo>) {
                    mView.addPhotos(photos)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    mView.showError(e.message)
                }

                override fun onComplete() {}
            })
    }

    override fun openPhotoDetails(photo: Photo?) {
        mView.showPhotoDetails(photo)
    }
}