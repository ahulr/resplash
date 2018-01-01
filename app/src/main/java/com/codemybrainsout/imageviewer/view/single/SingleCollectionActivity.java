package com.codemybrainsout.imageviewer.view.single;

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
import com.codemybrainsout.imageviewer.api.CollectionService;
import com.codemybrainsout.imageviewer.listener.EndlessRecyclerViewScrollListener;
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener;
import com.codemybrainsout.imageviewer.model.BaseModel;
import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.view.base.BaseActivity;
import com.codemybrainsout.imageviewer.view.detail.DetailActivity;
import com.codemybrainsout.imageviewer.view.photo.PhotoAdapter;
import com.codemybrainsout.imageviewer.view.user.UserActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleCollectionActivity extends BaseActivity implements SingleContract.View, RecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String EXTRA_COLLECTION = "extra_collection";

    @BindView(R.id.activity_single_toolbar)
    Toolbar activitySingleToolbar;
    @BindView(R.id.activity_single_swipe_refresh_layout)
    SwipeRefreshLayout activitySingleSwipeRefreshLayout;
    @BindView(R.id.activity_single_RV)
    RecyclerView activitySingleRV;
    @BindView(R.id.parent)
    LinearLayout parent;

    private int page = 1, limit = 20;

    @Inject
    public CollectionService collectionService;
    public Collection collection;

    @Inject
    public PhotoAdapter photoAdapter;
    private SinglePresenter singlePresenter;

    public static Intent getSingleCollectionIntent(Context context, Collection collection) {
        Intent intent = new Intent(context, SingleCollectionActivity.class);
        intent.putExtra(EXTRA_COLLECTION, collection);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_collection);
        ButterKnife.bind(this);
        setToolbar();

        SingleComponent singleComponent = DaggerSingleComponent.builder()
                .appComponent(getAppComponent())
                .singleModule(new SingleModule(this))
                .build();
        singleComponent.inject(this);

        activitySingleSwipeRefreshLayout.setOnRefreshListener(this);
        photoAdapter.setRecyclerViewItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        activitySingleRV.setLayoutManager(linearLayoutManager);
        activitySingleRV.setAdapter(photoAdapter);
        activitySingleRV.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                singlePresenter.loadMorePhotos(page, limit);
            }
        });

        if (getIntent().hasExtra(EXTRA_COLLECTION)) {
            collection = (Collection) getIntent().getSerializableExtra(EXTRA_COLLECTION);
            singlePresenter = new SinglePresenter(collectionService, this, collection);
            singlePresenter.loadCollection(collection);
            singlePresenter.loadPhotos(page, limit);
        }
    }

    @Override
    public void showLoading() {
        activitySingleSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (activitySingleSwipeRefreshLayout.isRefreshing()) {
            activitySingleSwipeRefreshLayout.setRefreshing(false);
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
    public void showCollectionDetails(Collection collection) {
        getSupportActionBar().setTitle(collection.getTitle());
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

        setSupportActionBar(activitySingleToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
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
        singlePresenter.openPhoto((Photo) baseModel);
    }

    @Override
    public void onUserClick(User user) {
        singlePresenter.openUser(user);
    }

    @Override
    public void onRefresh() {
        page = 1;
        singlePresenter.loadPhotos(page, limit);
    }
}
