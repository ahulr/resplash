package com.codemybrainsout.imageviewer.view.home;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.custom.SquareImageView;
import com.codemybrainsout.imageviewer.listener.FooterItemClickListener;
import com.codemybrainsout.imageviewer.model.BaseModel;
import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Footer;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahulr on 10-06-2017.
 */

public class MultiViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private final int VIEW_TYPE_PHOTO = 100;
    private final int VIEW_TYPE_COLLECTION = 101;
    private final int VIEW_TYPE_FOOTER = 102;
    List<BaseModel> itemList = new ArrayList<>();

    private RecyclerViewItemClickListener recyclerViewItemClickListener;
    private FooterItemClickListener footerItemClickListener;

    public MultiViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {

        if (itemList.get(position) instanceof Photo) {
            return VIEW_TYPE_PHOTO;
        } else if (itemList.get(position) instanceof Collection) {
            return VIEW_TYPE_COLLECTION;
        } else if (itemList.get(position) instanceof Footer) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_PHOTO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;

        switch (viewType) {
            case VIEW_TYPE_PHOTO:
                v = inflater.inflate(R.layout.item_photo_minimal, parent, false);
                viewHolder = new PhotoViewHolder(v);
                break;

            case VIEW_TYPE_COLLECTION:
                v = inflater.inflate(R.layout.item_collection_minimal, parent, false);
                viewHolder = new CollectionViewHolder(v);
                break;

            case VIEW_TYPE_FOOTER:
                v = inflater.inflate(R.layout.item_footer, parent, false);
                viewHolder = new FooterViewHolder(v);
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_PHOTO:
                setPhoto((PhotoViewHolder) holder, position);
                break;

            case VIEW_TYPE_COLLECTION:
                setCollection((CollectionViewHolder) holder, position);
                break;

            case VIEW_TYPE_FOOTER:
                setFooter((FooterViewHolder) holder, position);
                break;
        }
    }

    private void setFooter(FooterViewHolder holder, int position) {

        Footer footer = (Footer) itemList.get(position);
        if (footer.getType() == Footer.Type.Collection) {
            holder.itemFooterTV.setText(context.getString(R.string.browse_all_collections));
        } else {
            holder.itemFooterTV.setText(context.getString(R.string.browse_all_photos));
        }

    }

    private void setCollection(CollectionViewHolder holder, int position) {
        Collection collection = (Collection) itemList.get(position);
        holder.itemCollectionalMinimalIV.setBackgroundColor(Color.parseColor(collection.getCoverPhoto().getColor()));
        Glide.with(context)
                .load(collection.getCoverPhoto().getUrls().getSmall())
                .into(holder.itemCollectionalMinimalIV);
    }

    private void setPhoto(PhotoViewHolder holder, int position) {

        Photo photo = (Photo) itemList.get(position);
        holder.itemPhotoMinimalIV.setBackgroundColor(Color.parseColor(photo.getColor()));
        Glide.with(context)
                .load(photo.getUrls().getSmall())
                .into(holder.itemPhotoMinimalIV);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setFooter(Footer.Type type) {
        Footer footer = new Footer();
        footer.setType(type);
        itemList.add(footer);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addItem(BaseModel baseModel) {
        if (!itemList.contains(baseModel)) {
            itemList.add(baseModel);
            notifyItemInserted(itemList.size() - 1);
        } else {
            itemList.set(itemList.indexOf(baseModel), baseModel);
            notifyItemChanged(itemList.indexOf(baseModel));
        }
    }

    public void setFooterItemClickListener(FooterItemClickListener footerItemClickListener) {
        this.footerItemClickListener = footerItemClickListener;
    }

    public void setPhotos(List<Photo> list) {
        itemList.clear();
        notifyDataSetChanged();
        addPhotos(list);
    }

    public void addPhotos(List<Photo> list) {
        for (int i = 0; i < list.size(); i++) {
            addItem(list.get(i));
        }
    }

    public void addCollections(List<Collection> list) {
        for (int i = 0; i < list.size(); i++) {
            addItem(list.get(i));
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_photo_minimal_IV)
        SquareImageView itemPhotoMinimalIV;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (recyclerViewItemClickListener != null) {
                recyclerViewItemClickListener.onItemClick((Photo) itemList.get(getAdapterPosition()));
            }
        }
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_collection_minimal_IV)
        SquareImageView itemCollectionalMinimalIV;

        public CollectionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (recyclerViewItemClickListener != null) {
                recyclerViewItemClickListener.onItemClick((Collection) itemList.get(getAdapterPosition()));
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_footer_TV)
        public TextView itemFooterTV;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (footerItemClickListener != null) {
                footerItemClickListener.onFooterClick((Footer) itemList.get(getAdapterPosition()));
            }
        }
    }

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

}
