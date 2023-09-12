package com.codemybrainsout.imageviewer.view.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.codemybrainsout.imageviewer.R
import com.codemybrainsout.imageviewer.databinding.ItemCollectionMinimalBinding
import com.codemybrainsout.imageviewer.databinding.ItemFooterBinding
import com.codemybrainsout.imageviewer.databinding.ItemPhotoMinimalBinding
import com.codemybrainsout.imageviewer.listener.FooterItemClickListener
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener
import com.codemybrainsout.imageviewer.model.BaseModel
import com.codemybrainsout.imageviewer.model.Collection
import com.codemybrainsout.imageviewer.model.Footer
import com.codemybrainsout.imageviewer.model.Photo

/**
 * Created by ahulr on 10-06-2017.
 */
class MultiViewAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    companion object {
        private const val VIEW_TYPE_PHOTO = 100
        private const val VIEW_TYPE_COLLECTION = 101
        private const val VIEW_TYPE_FOOTER = 102
    }

    private var itemList: MutableList<BaseModel> = ArrayList()
    private var recyclerViewItemClickListener: RecyclerViewItemClickListener? = null
    private var footerItemClickListener: FooterItemClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position] is Photo) {
            VIEW_TYPE_PHOTO
        } else if (itemList[position] is Collection) {
            VIEW_TYPE_COLLECTION
        } else if (itemList[position] is Footer) {
            VIEW_TYPE_FOOTER
        } else {
            VIEW_TYPE_PHOTO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_PHOTO -> {
                val binding = ItemPhotoMinimalBinding.inflate(inflater, parent, false)
                PhotoViewHolder(binding)
            }

            VIEW_TYPE_COLLECTION -> {
                val binding = ItemCollectionMinimalBinding.inflate(inflater, parent, false)
                CollectionViewHolder(binding)
            }

            else -> {
                val binding = ItemFooterBinding.inflate(inflater, parent, false)
                val viewHolder = FooterViewHolder(binding)
                val layoutParams: StaggeredGridLayoutManager.LayoutParams =
                    viewHolder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                layoutParams.isFullSpan = true
                viewHolder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_PHOTO -> setPhoto(holder as PhotoViewHolder, position)
            VIEW_TYPE_COLLECTION -> setCollection(holder as CollectionViewHolder, position)
            VIEW_TYPE_FOOTER -> setFooter(holder as FooterViewHolder, position)
        }
    }

    private fun setFooter(holder: FooterViewHolder, position: Int) {
        val footer: Footer = itemList[position] as Footer
        holder.bind(footer, footerItemClickListener)
    }

    private fun setCollection(holder: CollectionViewHolder, position: Int) {
        val collection = itemList[position] as Collection
        holder.bind(collection, recyclerViewItemClickListener)

    }

    private fun setPhoto(holder: PhotoViewHolder, position: Int) {
        val photo = itemList[position] as Photo
        holder.bind(photo, recyclerViewItemClickListener)
    }

    fun setFooter(type: Footer.Type) {
        val footer = Footer()
        footer.type = type
        itemList.add(footer)
        notifyItemInserted(itemList.size - 1)
    }

    private fun addItem(baseModel: BaseModel) {
        if (!itemList.contains(baseModel)) {
            itemList.add(baseModel)
            notifyItemInserted(itemList.size - 1)
        } else {
            itemList[itemList.indexOf(baseModel)] = baseModel
            notifyItemChanged(itemList.indexOf(baseModel))
        }
    }

    fun setPhotos(list: List<Photo>) {
        itemList.clear()
        notifyDataSetChanged()
        addPhotos(list)
    }

    fun addPhotos(list: List<Photo>) {
        for (i in list.indices) {
            addItem(list[i])
        }
    }

    fun addCollections(list: List<Collection>) {
        for (i in list.indices) {
            addItem(list[i])
        }
    }

    fun setRecyclerViewItemClickListener(recyclerViewItemClickListener: RecyclerViewItemClickListener?) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener
    }

    fun setFooterItemClickListener(footerItemClickListener: FooterItemClickListener?) {
        this.footerItemClickListener = footerItemClickListener
    }

    override fun getItemCount() = itemList.size
}

internal class PhotoViewHolder(val binding: ItemPhotoMinimalBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: Photo, recyclerViewItemClickListener: RecyclerViewItemClickListener?) {
        binding.itemPhotoMinimalIV.setBackgroundColor(Color.parseColor(photo.color))
        Glide.with(binding.root.context)
            .load(photo.urls!!.small)
            .into(binding.itemPhotoMinimalIV)
        recyclerViewItemClickListener?.onItemClick(photo)
    }
}

internal class CollectionViewHolder(val binding: ItemCollectionMinimalBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(collection: Collection, recyclerViewItemClickListener: RecyclerViewItemClickListener?) {
        binding.itemCollectionMinimalIV.setBackgroundColor(
            Color.parseColor(
                collection.coverPhoto!!.color
            )
        )
        Glide.with(binding.root.context)
            .load(collection.coverPhoto!!.urls!!.small)
            .into(binding.itemCollectionMinimalIV)
        recyclerViewItemClickListener?.onItemClick(collection)
    }
}

internal class FooterViewHolder(val binding: ItemFooterBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(footer: Footer, footerItemClickListener: FooterItemClickListener?) {
        if (footer.type === Footer.Type.Collection) {
            binding.itemFooterTV.text = binding.root.context.getString(R.string.browse_all_collections)
        } else {
            binding.itemFooterTV.text = binding.root.context.getString(R.string.browse_all_photos)
        }

        itemView.setOnClickListener {
            footerItemClickListener?.onFooterClick(footer)
        }

    }
}