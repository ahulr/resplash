package com.codemybrainsout.imageviewer.view.collection;

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
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.view.base.BaseActivity;
import com.codemybrainsout.imageviewer.view.single.SingleCollectionActivity;
import com.codemybrainsout.imageviewer.view.user.UserActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CollectionActivity extends BaseActivity implements CollectionContract.View, RecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.activity_collection_toolbar)
    Toolbar activityCollectionToolbar;
    @BindView(R.id.activity_collection_swipe_refresh_layout)
    SwipeRefreshLayout activityCollectionSwipeRefreshLayout;
    @BindView(R.id.activity_collection_RV)
    RecyclerView activityCollectionRV;
    @BindView(R.id.activity_collection_RL)
    RelativeLayout activityCollectionRL;
    @BindView(R.id.parent)
    LinearLayout parent;

    @Inject
    public CollectionAdapter collectionAdapter;

    @Inject
    public CollectionPresenter collectionPresenter;

    private int page = 1, limit = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        setToolbar();

        CollectionComponent collectionComponent = DaggerCollectionComponent.builder()
                .appComponent(getAppComponent())
                .collectionModule(new CollectionModule(this)).build();
        collectionComponent.inject(this);

        collectionAdapter.setRecyclerViewItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        activityCollectionRV.setLayoutManager(linearLayoutManager);
        activityCollectionRV.setAdapter(collectionAdapter);
        activityCollectionSwipeRefreshLayout.setOnRefreshListener(this);
        activityCollectionRV.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                collectionPresenter.loadMoreCollections(page, limit);
            }
        });

        collectionPresenter.loadCollections(page, limit);
    }

    @Override
    public void showLoading() {
        activityCollectionSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (activityCollectionSwipeRefreshLayout.isRefreshing()) {
            activityCollectionSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showUser(User user) {
        Intent intent = UserActivity.getUserActivityIntent(this, user);
        startActivity(intent);
    }

    @Override
    public void showCollection(Collection collection) {
        Intent intent = SingleCollectionActivity.getSingleCollectionIntent(this, collection);
        startActivity(intent);
    }

    @Override
    public void refreshCollections(List<Collection> list) {
        collectionAdapter.setCollections(list);
    }

    @Override
    public void addCollections(List<Collection> list) {
        collectionAdapter.addCollections(list);
    }

    @Override
    public void showError(String s) {
        showErrorSnackbar(parent, s);
    }

    public static Intent getCollectionActivityIntent(Context context) {
        Intent intent = new Intent(context, CollectionActivity.class);
        return intent;
    }

    @Override
    public void setToolbar() {
        setSupportActionBar(activityCollectionToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_collection));
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
        collectionPresenter.openCollection((Collection) baseModel);
    }

    @Override
    public void onUserClick(User user) {
        collectionPresenter.openUser(user);
    }

    @Override
    public void onRefresh() {
        page = 1;
        collectionPresenter.loadCollections(page, limit);
    }
}
