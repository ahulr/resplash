package com.codemybrainsout.imageviewer.view.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.listener.EndlessRecyclerViewScrollListener;
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener;
import com.codemybrainsout.imageviewer.model.BaseModel;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.utility.PrefHelper;
import com.codemybrainsout.imageviewer.view.base.BaseActivity;
import com.codemybrainsout.imageviewer.view.detail.DetailActivity;
import com.codemybrainsout.imageviewer.view.home.MultiViewAdapter;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class SearchActivity extends BaseActivity implements SearchContract.View, MaterialSearchBar.OnSearchActionListener, RecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.activity_search_bar)
    MaterialSearchBar activitySearchBar;
    @BindView(R.id.activity_search_swipe_refresh_layout)
    SwipeRefreshLayout activitySearchSwipeRefreshLayout;
    @BindView(R.id.activity_search_RV)
    RecyclerView activitySearchRV;
    @BindView(R.id.activity_search_RL)
    RelativeLayout activitySearchRL;
    @BindView(R.id.parent)
    LinearLayout parent;

    @Inject
    public SearchPresenter searchPresenter;

    private List<String> lastSearches;
    private int page = 1, limit = 20;
    private static final String SEARCH = "search";

    @Inject
    public MultiViewAdapter multiViewAdapter;
    private String query;

    public static Intent getSearchIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        SearchComponent searchComponent = DaggerSearchComponent.builder()
                .appComponent(getAppComponent())
                .searchModule(new SearchModule(this))
                .build();
        searchComponent.inject(this);

        activitySearchSwipeRefreshLayout.setEnabled(false);
        activitySearchSwipeRefreshLayout.setOnRefreshListener(this);
        multiViewAdapter.setRecyclerViewItemClickListener(this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        activitySearchRV.setLayoutManager(staggeredGridLayoutManager);
        activitySearchRV.setAdapter(multiViewAdapter);
        activitySearchRV.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                searchPresenter.searchMorePhotos(query, page, limit);
            }
        });

        activitySearchBar.setSpeechMode(false);
        activitySearchBar.setNavButtonEnabled(true);
        activitySearchBar.setNavigationIcon(R.drawable.ic_arrow_back);
        activitySearchBar.setOnSearchActionListener(this);
        lastSearches = loadSearchSuggestionFromDisk();
        activitySearchBar.setLastSuggestions(lastSearches);
        activitySearchBar.performClick();
    }

    private List<String> loadSearchSuggestionFromDisk() {
        return PrefHelper.loadList(this, SEARCH);
    }

    private void saveSearchSuggestionToDisk(List<String> lastSuggestions) {
        PrefHelper.storeList(this, SEARCH, lastSuggestions);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //save last queries to disk
        saveSearchSuggestionToDisk(activitySearchBar.getLastSuggestions());
    }

    @Override
    public void setToolbar() {

    }

    @Override
    public void showLoading() {
        activitySearchSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (activitySearchSwipeRefreshLayout.isRefreshing()) {
            activitySearchSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showUser(User user) {

    }

    @Override
    public void showPhoto(Photo photo) {
        Intent intent = DetailActivity.getDetailActivityIntent(this, photo);
        startActivity(intent);
    }

    @Override
    public void refreshPhotos(List<Photo> list) {

        if (list.size() > 0) {
            activitySearchSwipeRefreshLayout.setEnabled(true);
        }

        multiViewAdapter.setPhotos(list);
    }

    @Override
    public void addPhotos(List<Photo> list) {
        multiViewAdapter.addPhotos(list);
    }

    @Override
    public void showError(String s) {
        showErrorSnackbar(parent, s);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        String s = enabled ? "enabled" : "disabled";
        Timber.i("Search " + s);
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        query = text.toString();
        searchPresenter.searchPhotos(text.toString(), page, limit);
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(BaseModel baseModel) {
        searchPresenter.openPhoto((Photo) baseModel);
    }

    @Override
    public void onUserClick(User user) {
        searchPresenter.openUser(user);
    }

    @Override
    public void onRefresh() {
        page = 1;
        searchPresenter.searchPhotos(query, page, limit);
    }
}
