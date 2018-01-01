package com.codemybrainsout.imageviewer.view.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.api.PhotoService;
import com.codemybrainsout.imageviewer.custom.CircleImageView;
import com.codemybrainsout.imageviewer.custom.ExifDialog;
import com.codemybrainsout.imageviewer.model.Exif;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;
import com.codemybrainsout.imageviewer.service.FileDownloadService;
import com.codemybrainsout.imageviewer.utility.Extra;
import com.codemybrainsout.imageviewer.view.base.BaseActivity;
import com.codemybrainsout.imageviewer.view.user.UserActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class DetailActivity extends BaseActivity implements DetailContract.View, EasyPermissions.PermissionCallbacks {

    private static final String EXTRA_PHOTO = "photo_extra";
    private static final String TAG = DetailActivity.class.getSimpleName();

    @BindView(R.id.activity_detail_user_IV)
    CircleImageView activityDetailUserIV;
    @BindView(R.id.activity_detail_user_TV)
    TextView activityDetailUserTV;
    @BindView(R.id.activity_detail_toolbar)
    Toolbar activityDetailToolbar;
    @BindView(R.id.activity_detail_IVT)
    ImageViewTouch activityDetailIVT;
    @BindView(R.id.activity_detail_user_LL)
    LinearLayout activityDetailUserLL;
    @BindView(R.id.parent)
    RelativeLayout parent;
    @BindView(R.id.activity_detail_set_wallpaper_IV)
    ImageView activityDetailSetWallpaperIV;
    @BindView(R.id.activity_detail_set_wallpaper)
    LinearLayout activityDetailSetWallpaper;
    @BindView(R.id.activity_detail_download)
    LinearLayout activityDetailDownload;
    @BindView(R.id.activity_detail_info_LL)
    LinearLayout activityDetailInfoLL;

    private Photo photo;

    @Inject
    public PhotoService photoService;

    public DetailPresenter mPresenter;

    private int PERMISSIONS = 100;
    private String[] per = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static Intent getDetailActivityIntent(Context context, Photo photo) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_PHOTO, photo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setToolbar();

        DetailComponent detailComponent = DaggerDetailComponent.builder()
                .appComponent(getAppComponent())
                .detailModule(new DetailModule(this))
                .build();
        detailComponent.inject(this);

        if (getIntent().hasExtra(EXTRA_PHOTO)) {
            photo = (Photo) getIntent().getSerializableExtra(EXTRA_PHOTO);
            mPresenter = new DetailPresenter(photoService, this, photo);
            mPresenter.loadPhoto();
        }
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
    public void showPhoto(final Photo photo) {

        this.photo = photo;
        final User user = photo.getUser();

        if (!TextUtils.isEmpty(user.getProfileImage().getMedium())) {
            Glide.with(this)
                    .load(user.getProfileImage().getMedium())
                    .into(activityDetailUserIV);
        }

        activityDetailUserTV.setText(user.getName());
        activityDetailUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openUserDetails(user);
            }
        });

        if (!TextUtils.isEmpty(photo.getUrls().getRegular())) {
            Glide.with(this)
                    .load(photo.getUrls().getRegular())
                    .placeholder(Color.parseColor(photo.getColor()))
                    .into(activityDetailIVT);
        }

        //photo.getLocation();
        //photo.getLikes();
        //photo.getDownloads();
        activityDetailInfoLL.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.activity_detail_set_wallpaper)
    public void setAsWallpaper() {
        if (EasyPermissions.hasPermissions(this, per)) {
            if (!TextUtils.isEmpty(photo.getUrls().getFull())) {
                activityDetailSetWallpaperIV.performClick();
                mPresenter.setAsWallpaper(this, photo.getUrls().getFull());
            }
        } else {
            request();
        }
    }

    @OnClick(R.id.activity_detail_download)
    public void downloadImage() {
        if (EasyPermissions.hasPermissions(this, per)) {
            if (!TextUtils.isEmpty(photo.getUrls().getFull())) {
                mPresenter.downloadPhoto(this, photo.getUrls().getFull());
            }
        } else {
            request();
        }
    }

    @OnClick(R.id.activity_detail_info_LL)
    public void openInfo() {
        Exif exif = photo.getExif();
        if (exif != null) {
            if (photo.getDownloads() != null) {
                exif.setDownloads(photo.getDownloads());
            }
            if (photo.getLikes() != null) {
                exif.setLikes(photo.getLikes());
            }
            mPresenter.showExif(exif);
        }
    }

    @Override
    public void showExif(Exif exif) {
        //show exif view
        new ExifDialog(this, exif).show();
    }

    @Override
    public void showUserDetails(User user) {
        Intent intent = UserActivity.getUserActivityIntent(this, user);
        startActivity(intent);
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        // Called when the broadcast is received
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean success = intent.getBooleanExtra(Extra.SUCCESS, false);
            boolean wallpaper = intent.getBooleanExtra(Extra.WALLPAPER, false);

            if (success) {

                if (wallpaper) {
                    showErrorSnackbar(parent, R.string.wallpaper_success);
                } else {
                    showErrorSnackbar(parent, R.string.download_success);
                }

            } else {

                showErrorSnackbar(parent, R.string.error);

            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //set filter to only when download is complete and register broadcast receiver
        IntentFilter filter = new IntentFilter(FileDownloadService.ACTION_DOWNLOAD_COMPLETE);
        LocalBroadcastManager.getInstance(this).registerReceiver(downloadReceiver, filter);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(downloadReceiver);
        super.onPause();
    }

    @Override
    public void showError(String s) {
        showErrorSnackbar(parent, s);
    }

    @Override
    public void setToolbar() {
        setSupportActionBar(activityDetailToolbar);
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

    private void request() {
        EasyPermissions.requestPermissions(this, getString(R.string.permission_rationale), PERMISSIONS, per);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (!EasyPermissions.hasPermissions(this, per)) {
            request();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
