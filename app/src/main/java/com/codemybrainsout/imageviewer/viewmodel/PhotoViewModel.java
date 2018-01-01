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
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.view.detail.DetailActivity;
import com.codemybrainsout.imageviewer.view.user.UserActivity;

/**
 *
 *  Viewmodel required for binding photo data to item_photo.xml
 *
 * Created by ahulr on 08-06-2017.
 */

public class PhotoViewModel extends BaseObservable {

    private Context context;
    public Photo photo;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public PhotoViewModel(Context context, Photo photo) {
        this.context = context;
        this.photo = photo;
    }

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    public int getColor() {
        return TextUtils.isEmpty(photo.getColor()) ? ContextCompat.getColor(context, R.color.grey) : Color.parseColor(photo.getColor());
    }

    public String getUser() {
        return TextUtils.isEmpty(photo.getUser().getName())?"":photo.getUser().getName();
    }

    public String getUsername() {
        return TextUtils.isEmpty(photo.getUser().getUsername())?"":"@" + photo.getUser().getUsername();
    }

    public String getImageUrl() {
        return TextUtils.isEmpty(photo.getUrls().getRegular())?"":photo.getUrls().getRegular();
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
        return TextUtils.isEmpty(photo.getUser().getProfileImage().getMedium()) ? "" : photo.getUser().getProfileImage().getMedium();
    }

    @BindingAdapter({"bind:userImageUrl"})
    public static void loadUserImage(ImageView view, String userImageUrl) {
        if (!TextUtils.isEmpty(userImageUrl)) {
            Glide.with(view.getContext())
                    .load(userImageUrl)
                    .into(view);
        }
    }

    public View.OnClickListener onClickPhoto() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewItemClickListener != null) {
                    recyclerViewItemClickListener.onItemClick(photo);
                }
            }
        };
    }

    public View.OnClickListener onClickUser() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewItemClickListener != null) {
                    recyclerViewItemClickListener.onUserClick(photo.getUser());
                }
            }
        };
    }
}
