package com.codemybrainsout.imageviewer.view.user;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.api.UserService;
import com.codemybrainsout.imageviewer.custom.CircleImageView;
import com.codemybrainsout.imageviewer.custom.SquareImageView;
import com.codemybrainsout.imageviewer.listener.EndlessRecyclerViewScrollListener;
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener;
import com.codemybrainsout.imageviewer.model.BaseModel;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.transformation.BlurTransformation;
import com.codemybrainsout.imageviewer.view.base.BaseActivity;
import com.codemybrainsout.imageviewer.view.detail.DetailActivity;
import com.codemybrainsout.imageviewer.view.home.MultiViewAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserActivity extends BaseActivity implements UserContract.View, RecyclerViewItemClickListener {

    private static final String EXTRA_USER = "extra_user";

    @BindView(R.id.activity_user_toolbar)
    Toolbar activityUserToolbar;
    @BindView(R.id.activity_user_background_IV)
    ImageView activityUserBackgroundIV;
    @BindView(R.id.activity_user_profile_IV)
    CircleImageView activityUserProfileIV;
    @BindView(R.id.activity_user_name_TV)
    TextView activityUserNameTV;
    @BindView(R.id.activity_users_followers_TV)
    TextView activityUsersFollowersTV;
    @BindView(R.id.activity_users_following_TV)
    TextView activityUsersFollowingTV;
    @BindView(R.id.activity_users_social_LL)
    LinearLayout activityUsersSocialLL;
    @BindView(R.id.activity_user_bio_TV)
    TextView activityUserBioTV;
    @BindView(R.id.activity_user_collapsing_toolbar)
    CollapsingToolbarLayout activityUserCollapsingToolbar;
    @BindView(R.id.activity_user_app_bar_layout)
    AppBarLayout activityUserAppBarLayout;
    @BindView(R.id.activity_user_photos_RV)
    RecyclerView activityUserPhotosRV;
    @BindView(R.id.parent)
    CoordinatorLayout parent;

    @Inject
    public MultiViewAdapter multiViewAdapter;

    @Inject
    public UserPresenter userPresenter;
    private int page = 1, limit = 20;
    private String username;

    public static Intent getUserActivityIntent(Context context, User user) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        setToolbar();

        UserComponent userComponent = DaggerUserComponent.builder()
                .appComponent(getAppComponent())
                .userModule(new UserModule(this))
                .build();
        userComponent.inject(this);

        multiViewAdapter.setRecyclerViewItemClickListener(this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        activityUserPhotosRV.setLayoutManager(staggeredGridLayoutManager);
        activityUserPhotosRV.setAdapter(multiViewAdapter);
        activityUserPhotosRV.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPresenter.loadMorePhotos(username, page, limit);
            }
        });

        if (getIntent().hasExtra(EXTRA_USER)) {
            User user = (User) getIntent().getSerializableExtra(EXTRA_USER);
            username = user.getUsername();
            userPresenter.loadUser(username);
        }
    }

    private void beginEnterTransition() {
        activityUserBackgroundIV.post(new Runnable() {
            @Override
            public void run() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    final Animator circularReveal = ViewAnimationUtils.createCircularReveal(activityUserBackgroundIV, activityUserBackgroundIV.getWidth() / 2, 0, 0, activityUserBackgroundIV.getWidth());
                    circularReveal.setInterpolator(new AccelerateInterpolator(1.5f));
                    circularReveal.setDuration(500);
                    circularReveal.start();
                }

            }
        });
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
    public void showUser(User user) {

        if (!TextUtils.isEmpty(user.getProfileImage().getLarge())) {
            Glide.with(this)
                    .load(user.getProfileImage().getLarge())
                    .into(activityUserProfileIV);
        }

        if (!TextUtils.isEmpty(user.getProfileImage().getLarge())) {
            Glide.with(this)
                    .load(user.getProfileImage().getLarge())
                    .bitmapTransform(new BlurTransformation(this, 15))
                    .into(activityUserBackgroundIV);
        }

        beginEnterTransition();

        activityUserCollapsingToolbar.setTitleEnabled(true);
        activityUserCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        activityUserCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        activityUserCollapsingToolbar.setTitle(TextUtils.isEmpty(user.getName()) ? "" : user.getName());

        activityUserNameTV.setText(TextUtils.isEmpty(user.getName()) ? "" : user.getName());
        activityUserBioTV.setText(TextUtils.isEmpty(user.getBio()) ? "" : user.getBio());
        activityUsersFollowersTV.setText(user.getFollowersCount() + " Followers");
        activityUsersFollowingTV.setText(user.getFollowingCount() + " Following");

        userPresenter.loadPhotos(user.getUsername(), page, limit);
    }

    @Override
    public void refreshPhotos(List<Photo> list) {
        multiViewAdapter.setPhotos(list);
    }

    @Override
    public void addPhotos(List<Photo> list) {
        multiViewAdapter.addPhotos(list);
    }

    @Override
    public void showPhotoDetails(Photo photo) {
        Intent intent = DetailActivity.getDetailActivityIntent(this, photo);
        startActivity(intent);
    }

    @Override
    public void showError(String s) {
        showErrorSnackbar(parent, s);
    }

    @Override
    public void onItemClick(BaseModel baseModel) {
        userPresenter.openPhotoDetails((Photo) baseModel);
    }

    @Override
    public void onUserClick(User user) {

    }

    @Override
    public void setToolbar() {

        setSupportActionBar(activityUserToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
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

}
