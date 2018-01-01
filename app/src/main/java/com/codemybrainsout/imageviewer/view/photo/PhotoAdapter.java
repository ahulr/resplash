package com.codemybrainsout.imageviewer.view.photo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.databinding.ItemPhotoBinding;
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.utility.Utils;
import com.codemybrainsout.imageviewer.viewmodel.PhotoViewModel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ahulr on 06-06-2017.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.BindingHolder> {


    private List<Photo> photoList = new ArrayList<>();
    private Context mContext;
    private int lastAnimatedPosition = -1;
    private final int ANIMATED_ITEMS_COUNT = 3;

    public PhotoAdapter(Context context) {
        this.mContext = context;
    }

    public RecyclerViewItemClickListener recyclerViewItemClickListener;

    @Override
    public PhotoAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPhotoBinding photoBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_photo,
                parent,
                false);
        return new BindingHolder(photoBinding);
    }

    //Used Data binding to set values directly to the view item
    @Override
    public void onBindViewHolder(PhotoAdapter.BindingHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        ItemPhotoBinding photoBinding = holder.binding;
        PhotoViewModel photoViewModel = new PhotoViewModel(mContext, photoList.get(position));
        photoViewModel.setRecyclerViewItemClickListener(recyclerViewItemClickListener);
        photoBinding.setViewModel(photoViewModel);
    }

    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(mContext));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(1000)
                    .start();
        }
    }

    public void setPhotos(List<Photo> list) {
        photoList.clear();
        notifyDataSetChanged();
        addPhotos(list);
    }

    public void addPhotos(List<Photo> list) {
        for (int i = 0; i < list.size(); i++) {
            addItem(list.get(i));
        }
    }

    public void addItem(Photo photo) {
        if (!photoList.contains(photo)) {
            photoList.add(photo);
            notifyItemInserted(photoList.size() - 1);
        } else {
            photoList.set(photoList.indexOf(photo), photo);
            notifyItemChanged(photoList.indexOf(photo));
        }
    }


    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }


    @Override
    public int getItemCount() {
        return photoList.size();
    }


    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemPhotoBinding binding;

        public BindingHolder(ItemPhotoBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }
    }

}
