package com.codemybrainsout.imageviewer.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.RelativeLayout;

import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.api.CollectionService;
import com.codemybrainsout.imageviewer.api.PhotoService;
import com.codemybrainsout.imageviewer.listener.FooterItemClickListener;
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener;
import com.codemybrainsout.imageviewer.model.BaseModel;
import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Footer;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.view.base.BaseActivity;
import com.codemybrainsout.imageviewer.view.collection.CollectionActivity;
import com.codemybrainsout.imageviewer.view.detail.DetailActivity;
import com.codemybrainsout.imageviewer.view.photo.PhotoActivity;
import com.codemybrainsout.imageviewer.view.search.SearchActivity;
import com.codemybrainsout.imageviewer.view.single.SingleCollectionActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class HomeActivity extends BaseActivity implements HomeContract.View, RecyclerViewItemClickListener, FooterItemClickListener {

    @BindView(R.id.activity_home_RV)
    RecyclerView activityHomeRV;
    @BindView(R.id.activity_home_search_CV)
    CardView activityHomeSearchCV;
    @BindView(R.id.parent)
    RelativeLayout parent;

    @Inject
    public MultiViewAdapter multiViewAdapter;

    @Inject
    public HomePresenter homePresenter;

    int limit = 6;
    String orderBy = "latest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        HomeComponent homeComponent = DaggerHomeComponent.builder()
                .appComponent(getAppComponent())
                .homeModule(new HomeModule(this))
                .build();
        homeComponent.inject(this);

        multiViewAdapter.setRecyclerViewItemClickListener(this);
        multiViewAdapter.setFooterItemClickListener(this);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        activityHomeRV.setLayoutManager(staggeredGridLayoutManager);
        activityHomeRV.setAdapter(multiViewAdapter);

        homePresenter.loadPhotos(limit, orderBy);
    }

    @Override
    public void showLoading() {
        addProgressLayout(this, parent);
    }

    @Override
    public void hideLoading() {
        removeProgressLayout(parent);
    }

    @Override
    public void setPhotos(List<Photo> list) {
        homePresenter.loadCollections(limit);
        multiViewAdapter.addPhotos(list);
        multiViewAdapter.setFooter(Footer.Type.Photo);
    }

    @Override
    public void setCollections(List<Collection> list) {
        multiViewAdapter.addCollections(list);
        multiViewAdapter.setFooter(Footer.Type.Collection);
    }

    @Override
    public void openAllCollections() {
        Intent intent = CollectionActivity.getCollectionActivityIntent(this);
        startActivity(intent);
    }

    @Override
    public void openAllPhotos() {
        Intent intent = PhotoActivity.getPhotoActivityIntent(this);
        startActivity(intent);
    }

    @Override
    public void showError(String s) {
        showErrorSnackbar(parent, s);
    }

    @Override
    public void showPhoto(Photo photo) {
        Intent intent = DetailActivity.getDetailActivityIntent(this, photo);
        startActivity(intent);
    }

    @Override
    public void showCollection(Collection collection) {
        Intent intent = SingleCollectionActivity.getSingleCollectionIntent(this, collection);
        startActivity(intent);
    }

    @Override
    public void onFooterClick(Footer footer) {
        Footer.Type type = footer.getType();
        if (type == Footer.Type.Collection) {
            homePresenter.viewAllCollections();
        } else {
            homePresenter.viewAllPhotos();
        }

    }

    @Override
    public void onItemClick(BaseModel baseModel) {

        if (baseModel instanceof Photo) {
            homePresenter.openPhoto((Photo) baseModel);
        } else if (baseModel instanceof Collection) {
            homePresenter.openCollection((Collection) baseModel);
        }

    }

    @Override
    public void onUserClick(User user) {

    }

    @Override
    public void setToolbar() {

    }

    @OnClick(R.id.activity_home_search_CV)
    public void openSearch() {
        Intent intent = SearchActivity.getSearchIntent(this);
        startActivity(intent);
    }
}
