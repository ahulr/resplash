package com.codemybrainsout.imageviewer.view.detail

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Color.parseColor
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.codemybrainsout.imageviewer.R
import com.codemybrainsout.imageviewer.custom.ExifDialog
import com.codemybrainsout.imageviewer.databinding.ActivityDetailBinding
import com.codemybrainsout.imageviewer.extensions.addProgressLayout
import com.codemybrainsout.imageviewer.extensions.removeProgressLayout
import com.codemybrainsout.imageviewer.extensions.showErrorSnackbar
import com.codemybrainsout.imageviewer.extensions.viewBinding
import com.codemybrainsout.imageviewer.model.Exif
import com.codemybrainsout.imageviewer.model.Photo
import com.codemybrainsout.imageviewer.model.User
import com.codemybrainsout.imageviewer.service.FileDownloadService
import com.codemybrainsout.imageviewer.utility.Extra
import com.codemybrainsout.imageviewer.view.base.BaseActivity
import com.codemybrainsout.imageviewer.view.user.UserActivity
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import com.vmadalin.easypermissions.models.PermissionRequest
import javax.inject.Inject

class DetailActivity : BaseActivity(), DetailContract.View, EasyPermissions.PermissionCallbacks {

    private val binding by viewBinding(ActivityDetailBinding::inflate)

    @Inject
    lateinit var mPresenter: DetailPresenter

    private var photo: Photo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(EXTRA_PHOTO)) {
            photo = intent.getParcelableExtra(EXTRA_PHOTO)
            loadDetails()
        }

        binding.apply {
            layoutSetWallpaper.setOnClickListener { setAsWallpaper() }
            layoutDownload.setOnClickListener { downloadImage() }
            layoutInfo.setOnClickListener { openInfo() }
            toolbar.setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun loadDetails() {
        photo?.id?.let { mPresenter.loadPhoto(it) }
    }

    override fun showLoading() {
        binding.root.addProgressLayout(this)
    }

    override fun hideLoading() {
        binding.root.removeProgressLayout()
    }

    override fun showPhoto(photo: Photo) {
        this.photo = photo
        photo.user?.let { user ->
            if (user.profileImage?.medium.isNullOrEmpty().not()) {
                Glide.with(this)
                    .load(user.profileImage?.medium)
                    .into(binding.imageViewUser)
            }
            binding.textViewUser.text = user.name
            binding.layoutUser.setOnClickListener {
                mPresenter.openUserDetails(user)
            }
        }

        val color = parseColor(photo.color)
        if (photo.urls?.regular.isNullOrEmpty().not()) {
            Glide.with(this)
                .load(photo.urls?.regular)
                .placeholder(ColorDrawable(color))
                .into(binding.imageViewTouch)
        }

        binding.layoutInfo.visibility = View.VISIBLE
    }

    @AfterPermissionGranted(PERMISSIONS_WALLPAPER)
    private fun setAsWallpaper() {
        if (EasyPermissions.hasPermissions(this, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)) {
            photo?.urls?.full?.let {
                if (it.isNotEmpty()) {
                    binding.imageViewSetWallpaper.performClick()
                    mPresenter.setAsWallpaper(this, it)
                }
            }
        } else {
            request(PERMISSIONS_WALLPAPER)
        }
    }

    @AfterPermissionGranted(PERMISSIONS_DOWNLOAD)
    private fun downloadImage() {
        if (EasyPermissions.hasPermissions(this, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)) {
            photo?.urls?.full?.let {
                if (it.isNotEmpty()) {
                    mPresenter.downloadPhoto(this, it)
                }
            }
        } else {
            request(PERMISSIONS_DOWNLOAD)
        }
    }

    private fun openInfo() {
        val exif: Exif? = photo?.exif
        if (exif != null) {
            if (photo?.downloads != null) {
                exif.downloads = photo?.downloads
            }
            if (photo?.likes != null) {
                exif.likes = photo?.likes
            }
            mPresenter.showExif(exif)
        }
    }

    override fun showExif(exif: Exif?) {
        //show exif view
        exif?.let { ExifDialog(this, it).show() }
    }

    override fun showUserDetails(user: User?) {
        val intent: Intent = UserActivity.getUserActivityIntent(this, user)
        startActivity(intent)
    }

    private val downloadReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        // Called when the broadcast is received
        override fun onReceive(context: Context, intent: Intent) {
            val success: Boolean = intent.getBooleanExtra(Extra.SUCCESS, false)
            val wallpaper: Boolean = intent.getBooleanExtra(Extra.WALLPAPER, false)
            if (success) {
                if (wallpaper) {
                    binding.root.showErrorSnackbar(R.string.wallpaper_success)
                } else {
                    binding.root.showErrorSnackbar(R.string.download_success)
                }
            } else {
                binding.root.showErrorSnackbar(R.string.error)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //set filter to only when download is complete and register broadcast receiver
        val filter = IntentFilter(FileDownloadService.ACTION_DOWNLOAD_COMPLETE)
        LocalBroadcastManager.getInstance(this).registerReceiver(downloadReceiver, filter)
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(downloadReceiver)
        super.onPause()
    }

    override fun showError(s: String?) {
        binding.root.showErrorSnackbar(s ?: "")
    }

    private fun request(requestCode: Int) {
        if (!EasyPermissions.hasPermissions(this, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)) {
            val request = PermissionRequest.Builder(this)
                .code(requestCode)
                .perms(arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE))
                .rationale(R.string.permission_rationale)
                .build()
            EasyPermissions.requestPermissions(this, request)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        // do nothing
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        private const val PERMISSIONS_WALLPAPER = 100
        private const val PERMISSIONS_DOWNLOAD = 100
        private const val EXTRA_PHOTO = "photo_extra"
        private val TAG = DetailActivity::class.java.simpleName
        fun getDetailActivityIntent(context: Context?, photo: Photo?): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_PHOTO, photo)
            return intent
        }
    }
}