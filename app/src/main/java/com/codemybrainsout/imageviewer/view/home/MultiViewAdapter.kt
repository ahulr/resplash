package com.codemybrainsout.imageviewer.view.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.codemybrainsout.imageviewer.R
import com.codemybrainsout.imageviewer.databinding.ItemCollectionMinimalBinding
import com.codemybrainsout.imageviewer.databinding.ItemPhotoMinimalBinding
import com.codemybrainsout.imageviewer.listener.FooterItemClickListener
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener
import com.codemybrainsout.imageviewer.model.BaseModel
import com.codemybrainsout.imageviewer.model.Collection
import com.codemybrainsout.imageviewer.model.Footer
import com.codemybrainsout.imageviewer.model.Photo
import java.util.ArrayList

/**
 * Created by ahulr on 10-06-2017.
 */
class MultiViewAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    companion object {
        private const val VIEW_TYPE_PHOTO = 100
        private const val VIEW_TYPE_COLLECTION = 101
        private const val VIEW_TYPE_FOOTER = 102
    }

    var itemList: MutableList<BaseModel> = ArrayList<BaseModel>()
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
        val v: View
        return when (viewType) {
            VIEW_TYPE_PHOTO -> {
                v = inflater.inflate(R.layout.item_photo_minimal, parent, false)
                PhotoViewHolder(v)
            }
            VIEW_TYPE_COLLECTION -> {
                v = inflater.inflate(R.layout.item_collection_minimal, parent, false)
                CollectionViewHolder(v)
            }
            VIEW_TYPE_FOOTER -> {
                v = inflater.inflate(R.layout.item_footer, parent, false)
                val viewHolder = FooterViewHolder(v)
                val layoutParams: StaggeredGridLayoutManager.LayoutParams =
                    viewHolder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                layoutParams.isFullSpan = true
                viewHolder
            }
            else -> {
                null
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
        if (footer.type === Footer.Type.Collection) {
            holder.itemFooterTV.setText(context.getString(R.string.browse_all_collections))
        } else {
            holder.itemFooterTV.setText(context.getString(R.string.browse_all_photos))
        }
    }

    private fun setCollection(holder: CollectionViewHolder, position: Int) {
        val collection = itemList[position] as Collection
        holder.itemCollectionalMinimalIV.setBackgroundColor(
            Color.parseColor(
                collection.coverPhoto!!.color
            )
        )
        Glide.with(context)
            .load(collection.coverPhoto!!.urls!!.small)
            .into(holder.itemCollectionalMinimalIV)
    }

    private fun setPhoto(holder: PhotoViewHolder, position: Int) {
        val photo = itemList[position] as Photo
        holder.itemPhotoMinimalIV.setBackgroundColor(Color.parseColor(photo.color))
        Glide.with(context)
            .load(photo.urls!!.small)
            .into(holder.itemPhotoMinimalIV)
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

    fun setFooterItemClickListener(footerItemClickListener: FooterItemClickListener?) {
        this.footerItemClickListener = footerItemClickListener
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

    inner class PhotoViewHolder(binding: ItemPhotoMinimalBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        @BindView(R.id.item_photo_minimal_IV)
        var itemPhotoMinimalIV: SquareImageView? = null
        override fun onClick(view: View) {
            if (recyclerViewItemClickListener != null) {
                recyclerViewItemClickListener.onItemClick(itemList[getAdapterPosition()] as Photo)
            }
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
        }
    }

    inner class CollectionViewHolder(binding: ItemCollectionMinimalBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        @BindView(R.id.item_collection_minimal_IV)
        var itemCollectionalMinimalIV: SquareImageView? = null
        override fun onClick(view: View) {
            if (recyclerViewItemClickListener != null) {
                recyclerViewItemClickListener.onItemClick(itemList[getAdapterPosition()] as Collection)
            }
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        @BindView(R.id.item_footer_TV)
        var itemFooterTV: TextView? = null
        override fun onClick(view: View) {
            if (footerItemClickListener != null) {
                footerItemClickListener.onFooterClick(itemList[getAdapterPosition()] as Footer)
            }
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
        }
    }

    fun setRecyclerViewItemClickListener(recyclerViewItemClickListener: RecyclerViewItemClickListener?) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener
    }

    override fun getItemCount() = itemList.size
}