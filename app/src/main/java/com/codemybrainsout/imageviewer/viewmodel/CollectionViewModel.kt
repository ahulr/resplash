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
import com.codemybrainsout.imageviewer.model.Collection

/**
 *
 * Viewmodel required for binding collection data to item_collection.xml
 *
 * Created by ahulr on 08-06-2017.
 */
class CollectionViewModel(private val context: Context, var collection: Collection) :
    BaseObservable() {

    private var recyclerViewItemClickListener: RecyclerViewItemClickListener? = null

    fun setRecyclerViewItemClickListener(recyclerViewItemClickListener: RecyclerViewItemClickListener?) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener
    }

    val color: Int
        get() = if (TextUtils.isEmpty(collection.coverPhoto!!.color)) ContextCompat.getColor(
            context,
            R.color.grey
        ) else Color.parseColor(
            collection.coverPhoto!!.color
        )
    val user: String
        get() = if (TextUtils.isEmpty(collection.user!!.name)) "" else collection.user!!.name!!
    val username: String
        get() = if (TextUtils.isEmpty(collection.user!!.username)) "" else "@" + collection.user!!.username
    val title: String
        get() = if (TextUtils.isEmpty(collection.title)) "" else collection.title!!
    val description: String
        get() = if (TextUtils.isEmpty(collection.description)) "" else collection.description!!
    val imageUrl: String
        get() = if (TextUtils.isEmpty(collection.coverPhoto!!.urls!!.regular)) "" else collection.coverPhoto!!.urls!!.regular!!
    val userImageUrl: String
        get() = if (TextUtils.isEmpty(collection.user!!.profileImage!!.medium)) "" else collection.user!!.profileImage!!.medium!!

    fun onClickCollection(): View.OnClickListener {
        return View.OnClickListener {
            if (recyclerViewItemClickListener != null) {
                recyclerViewItemClickListener?.onItemClick(collection)
            }
        }
    }

    fun onClickUser(): View.OnClickListener {
        return View.OnClickListener {
            if (recyclerViewItemClickListener != null) {
                recyclerViewItemClickListener?.onUserClick(collection.user)
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