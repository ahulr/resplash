package com.codemybrainsout.imageviewer.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener;
import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.view.user.UserActivity;

/**
 *
 * Viewmodel required for binding collection data to item_collection.xml
 *
 * Created by ahulr on 08-06-2017.
 */

public class CollectionViewModel extends BaseObservable {

    private Context context;
    public Collection collection;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public CollectionViewModel(Context context, Collection collection) {
        this.context = context;
        this.collection = collection;
    }

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    public int getColor() {
        return TextUtils.isEmpty(collection.getCoverPhoto().getColor()) ? ContextCompat.getColor(context, R.color.grey) : Color.parseColor(collection.getCoverPhoto().getColor());
    }

    public String getUser() {
        return TextUtils.isEmpty(collection.getUser().getName()) ? "" : collection.getUser().getName();
    }

    public String getUsername() {
        return TextUtils.isEmpty(collection.getUser().getUsername()) ? "" : "@" + collection.getUser().getUsername();
    }

    public String getTitle() {
        return TextUtils.isEmpty(collection.getTitle()) ? "" : collection.getTitle();
    }

    public String getDescription() {
        return TextUtils.isEmpty(collection.getDescription()) ? "" : collection.getDescription();
    }

    public String getImageUrl() {
        return TextUtils.isEmpty(collection.getCoverPhoto().getUrls().getRegular()) ? "" : collection.getCoverPhoto().getUrls().getRegular();
    }

    @BindingAdapter(value = {"imageUrl", "color"}, requireAll = false)
    public static void loadImage(ImageView view, String imageUrl, int color) {

        view.setBackgroundColor(color);
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(view.getContext())
                    .load(imageUrl)
                    //.placeholder(Color.parseColor(color))
                    .into(view);
        }
    }

    public String getUserImageUrl() {
        return TextUtils.isEmpty(collection.getUser().getProfileImage().getMedium()) ? "" : collection.getUser().getProfileImage().getMedium();
    }

    @BindingAdapter({"bind:userImageUrl"})
    public static void loadUserImage(ImageView view, String userImageUrl) {
        if (!TextUtils.isEmpty(userImageUrl)) {
            Glide.with(view.getContext())
                    .load(userImageUrl)
                    .into(view);
        }
    }

    public View.OnClickListener onClickCollection() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewItemClickListener != null) {
                    recyclerViewItemClickListener.onItemClick(collection);
                }
            }
        };
    }

    public View.OnClickListener onClickUser() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewItemClickListener != null) {
                    recyclerViewItemClickListener.onUserClick(collection.getUser());
                }
            }
        };
    }
}
