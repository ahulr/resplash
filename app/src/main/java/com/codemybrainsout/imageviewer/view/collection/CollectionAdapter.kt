package com.codemybrainsout.imageviewer.view.collection

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.codemybrainsout.imageviewer.R
import com.codemybrainsout.imageviewer.databinding.ItemCollectionBinding
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener
import com.codemybrainsout.imageviewer.model.Collection
import com.codemybrainsout.imageviewer.utility.Utils
import com.codemybrainsout.imageviewer.viewmodel.CollectionViewModel

/**
 * Created by ahulr on 06-06-2017.
 */
class CollectionAdapter(private val mContext: Context) : RecyclerView.Adapter<CollectionAdapter.BindingHolder?>() {

    companion object {
        private const val ANIMATED_ITEMS_COUNT = 2
    }

    private val collectionList: MutableList<Collection> = ArrayList()
    private var lastAnimatedPosition = -1
    var recyclerViewItemClickListener: RecyclerViewItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val collectionBinding: ItemCollectionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_collection,
            parent,
            false
        )
        return BindingHolder(collectionBinding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        runEnterAnimation(holder.itemView, position)
        val collectionBinding = holder.binding
        val collectionViewModel = CollectionViewModel(mContext, collectionList[position])
        collectionViewModel.setRecyclerViewItemClickListener(recyclerViewItemClickListener)
        collectionBinding.viewModel = collectionViewModel
    }

    private fun runEnterAnimation(view: View, position: Int) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return
        }
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position
            view.translationY =
                Utils.getScreenHeight(mContext).toFloat()
            view.animate()
                .translationY(0f)
                .setInterpolator(DecelerateInterpolator(3f))
                .setDuration(1000)
                .start()
        }
    }

    private fun addItem(collection: Collection) {
        if (!collectionList.contains(collection)) {
            collectionList.add(collection)
            notifyItemInserted(collectionList.size - 1)
        } else {
            collectionList[collectionList.indexOf(collection)] = collection
            notifyItemChanged(collectionList.indexOf(collection))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCollections(list: List<Collection>) {
        collectionList.clear()
        notifyDataSetChanged()
        addCollections(list)
    }

    fun addCollections(list: List<Collection>) {
        for (i in list.indices) {
            addItem(list[i])
        }
    }

    @JvmName("setRecyclerViewItemClickListener1")
    fun setRecyclerViewItemClickListener(recyclerViewItemClickListener: RecyclerViewItemClickListener?) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener
    }

    class BindingHolder(val binding: ItemCollectionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = collectionList.size

}