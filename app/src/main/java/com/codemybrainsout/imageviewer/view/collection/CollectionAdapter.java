package com.codemybrainsout.imageviewer.view.collection;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.databinding.ItemCollectionBinding;
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener;
import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.utility.Utils;
import com.codemybrainsout.imageviewer.viewmodel.CollectionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahulr on 06-06-2017.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.BindingHolder> {

    private List<Collection> collectionList = new ArrayList<>();
    private Context mContext;

    private int lastAnimatedPosition=-1;
    private final int ANIMATED_ITEMS_COUNT = 2;

    public CollectionAdapter(Context context) {
        this.mContext = context;
    }

    public RecyclerViewItemClickListener recyclerViewItemClickListener;

    @Override
    public CollectionAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCollectionBinding collectionBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_collection,
                parent,
                false);
        return new BindingHolder(collectionBinding);
    }

    //Used Data binding to set values directly to the view item
    @Override
    public void onBindViewHolder(CollectionAdapter.BindingHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        ItemCollectionBinding collectionBinding = holder.binding;
        CollectionViewModel collectionViewModel = new CollectionViewModel(mContext, collectionList.get(position));
        collectionViewModel.setRecyclerViewItemClickListener(recyclerViewItemClickListener);
        collectionBinding.setViewModel(collectionViewModel);
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


    public void addItem(Collection collection) {
        if (!collectionList.contains(collection)) {
            collectionList.add(collection);
            notifyItemInserted(collectionList.size() - 1);
        } else {
            collectionList.set(collectionList.indexOf(collection), collection);
            notifyItemChanged(collectionList.indexOf(collection));
        }
    }

    public void setCollections(List<Collection> list) {
        collectionList.clear();
        notifyDataSetChanged();
        addCollections(list);
    }

    public void addCollections(List<Collection> list) {
        for (int i = 0; i < list.size(); i++) {
            addItem(list.get(i));
        }
    }


    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }


    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemCollectionBinding binding;

        public BindingHolder(ItemCollectionBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }
    }

}
