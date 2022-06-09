package com.codemybrainsout.imageviewer.view.photo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.codemybrainsout.imageviewer.R
import com.codemybrainsout.imageviewer.databinding.ItemPhotoBinding
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener
import com.codemybrainsout.imageviewer.model.Photo
import com.codemybrainsout.imageviewer.utility.Utils
import com.codemybrainsout.imageviewer.viewmodel.PhotoViewModel
import java.util.ArrayList

/**
 * Created by ahulr on 06-06-2017.
 */
class PhotoAdapter(private val mContext: Context?) : RecyclerView.Adapter<PhotoAdapter.BindingHolder?>() {

    companion object {
        private const val ANIMATED_ITEMS_COUNT = 3
    }

    private val photoList: MutableList<Photo> = ArrayList()
    private var lastAnimatedPosition = -1
    var recyclerViewItemClickListener: RecyclerViewItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val photoBinding: ItemPhotoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_photo,
            parent,
            false
        )
        return BindingHolder(photoBinding)
    }

    //Used Data binding to set values directly to the view item
    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        runEnterAnimation(holder.itemView, position)
        val photoBinding = holder.binding
        val photoViewModel = PhotoViewModel(mContext, photoList[position])
        photoViewModel.setRecyclerViewItemClickListener(recyclerViewItemClickListener)
        photoBinding.viewModel = photoViewModel
    }

    private fun runEnterAnimation(view: View, position: Int) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return
        }
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position
            view.translationY =
                Utils.getScreenHeight(mContext!!).toFloat()
            view.animate()
                .translationY(0f)
                .setInterpolator(DecelerateInterpolator(3f))
                .setDuration(1000)
                .start()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPhotos(list: List<Photo>) {
        photoList.clear()
        notifyDataSetChanged()
        addPhotos(list)
    }

    fun addPhotos(list: List<Photo>) {
        for (i in list.indices) {
            addItem(list[i])
        }
    }

    private fun addItem(photo: Photo) {
        if (!photoList.contains(photo)) {
            photoList.add(photo)
            notifyItemInserted(photoList.size - 1)
        } else {
            photoList[photoList.indexOf(photo)] = photo
            notifyItemChanged(photoList.indexOf(photo))
        }
    }

    @JvmName("setRecyclerViewItemClickListener1")
    fun setRecyclerViewItemClickListener(recyclerViewItemClickListener: RecyclerViewItemClickListener?) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener
    }

    override fun getItemCount() = photoList.size

    class BindingHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}