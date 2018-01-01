package com.codemybrainsout.imageviewer.view.photo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.api.PhotoService;
import com.codemybrainsout.imageviewer.listener.EndlessRecyclerViewScrollListener;
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener;
import com.codemybrainsout.imageviewer.model.BaseModel;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.view.base.BaseActivity;
import com.codemybrainsout.imageviewer.view.detail.DetailActivity;
import com.codemybrainsout.imageviewer.view.user.UserActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends BaseActivity implements PhotoContract.View, RecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.activity_photo_toolbar)
    Toolbar activityPhotoToolbar;
    @BindView(R.id.activity_photo_swipe_refresh_layout)
    SwipeRefreshLayout activityPhotoSwipeRefreshLayout;
    @BindView(R.id.activity_photo_RV)
    RecyclerView activityPhotoRV;
    @BindView(R.id.activity_photo_RL)
    RelativeLayout activityPhotoRL;
    @BindView(R.id.parent)
    LinearLayout parent;

    @Inject
    public PhotoAdapter photoAdapter;

    @Inject
    public PhotoService photoService;

    @Inject
    public PhotoPresenter photoPresenter;

    int page = 1, limit = 20;
    String orderBy = "latest";

    public static Intent getPhotoActivityIntent(Context context) {
        Intent intent = new Intent(context, PhotoActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        setToolbar();

        PhotoComponent photoComponent = DaggerPhotoComponent.builder()
                .appComponent(getAppComponent())
                .photoModule(new PhotoModule(this))
                .build();
        photoComponent.inject(this);

        activityPhotoSwipeRefreshLayout.setOnRefreshListener(this);
        photoAdapter.setRecyclerViewItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        activityPhotoRV.setLayoutManager(linearLayoutManager);
        activityPhotoRV.setAdapter(photoAdapter);
        activityPhotoRV.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                photoPresenter.loadMorePhotos(page, limit, orderBy);

            }
        });

        photoPresenter.loadPhotos(page, limit, orderBy);
    }

    @Override
    public void showLoading() {
        activityPhotoSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (activityPhotoSwipeRefreshLayout.isRefreshing()) {
            activityPhotoSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showUser(User user) {
        Intent intent = UserActivity.getUserActivityIntent(this, user);
        startActivity(intent);
    }

    @Override
    public void showPhoto(Photo photo) {
        Intent intent = DetailActivity.getDetailActivityIntent(this, photo);
        startActivity(intent);
    }

    @Override
    public void refreshPhotos(List<Photo> list) {
        photoAdapter.setPhotos(list);
    }

    @Override
    public void addPhotos(List<Photo> list) {
        photoAdapter.addPhotos(list);
    }

    @Override
    public void showError(String s) {
        showErrorSnackbar(parent, s);
    }

    @Override
    public void setToolbar() {

        setSupportActionBar(activityPhotoToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_photo));
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(BaseModel baseModel) {
        photoPresenter.openPhoto((Photo) baseModel);
    }

    @Override
    public void onUserClick(User user) {
        photoPresenter.openUser(user);
    }

    @Override
    public void onRefresh() {
        page = 1;
        photoPresenter.loadPhotos(page, limit, orderBy);
    }
}
