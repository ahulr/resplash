package com.codemybrainsout.imageviewer.viewmodel

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.codemybrainsout.imageviewer.R
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener
import com.codemybrainsout.imageviewer.model.Photo

/**
 *
 * Viewmodel required for binding photo data to item_photo.xml
 *
 * Created by ahulr on 08-06-2017.
 */
class PhotoViewModel(private val context: Context, var photo: Photo) : BaseObservable() {

    private var recyclerViewItemClickListener: RecyclerViewItemClickListener? = null
    fun setRecyclerViewItemClickListener(recyclerViewItemClickListener: RecyclerViewItemClickListener?) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener
    }

    val color: Int
        get() = if (TextUtils.isEmpty(photo.color)) ContextCompat.getColor(
            context,
            R.color.grey
        ) else Color.parseColor(
            photo.color
        )
    val user: String
        get() = if (TextUtils.isEmpty(photo.user!!.name)) "" else photo.user!!.name!!
    val username: String
        get() = if (TextUtils.isEmpty(photo.user!!.username)) "" else "@" + photo.user!!.username
    val imageUrl: String
        get() = if (TextUtils.isEmpty(photo.urls!!.regular)) "" else photo.urls!!.regular!!
    val userImageUrl: String
        get() = if (TextUtils.isEmpty(photo.user!!.profileImage!!.medium)) "" else photo.user!!.profileImage!!.medium!!

    fun onClickPhoto(): View.OnClickListener {
        return View.OnClickListener {
            if (recyclerViewItemClickListener != null) {
                recyclerViewItemClickListener?.onItemClick(photo)
            }
        }
    }

    fun onClickUser(): View.OnClickListener {
        return View.OnClickListener {
            if (recyclerViewItemClickListener != null) {
                recyclerViewItemClickListener?.onUserClick(photo.user)
            }
        }
    }

    companion object {
        @BindingAdapter(value = ["imageUrl", "color"], requireAll = false)
        fun loadImage(view: ImageView, imageUrl: String?, color: Int) {
            view.setBackgroundColor(color)
            if (!TextUtils.isEmpty(imageUrl)) {
                Glide.with(view.context)
                    .load(imageUrl) //.placeholder(Color.parseColor(color))
                    .into(view)
            }
        }

        @BindingAdapter(value = ["bind:userImageUrl"])
        fun loadUserImage(view: ImageView, userImageUrl: String?) {
            if (!TextUtils.isEmpty(userImageUrl)) {
                Glide.with(view.context)
                    .load(userImageUrl)
                    .into(view)
            }
        }
    }
}