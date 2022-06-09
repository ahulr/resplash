package com.codemybrainsout.imageviewer.view.photo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codemybrainsout.imageviewer.R
import com.codemybrainsout.imageviewer.databinding.ActivityPhotoBinding
import com.codemybrainsout.imageviewer.extensions.showErrorSnackbar
import com.codemybrainsout.imageviewer.extensions.viewBinding
import com.codemybrainsout.imageviewer.listener.EndlessRecyclerViewScrollListener
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener
import com.codemybrainsout.imageviewer.model.BaseModel
import com.codemybrainsout.imageviewer.model.Photo
import com.codemybrainsout.imageviewer.model.User
import com.codemybrainsout.imageviewer.view.detail.DetailActivity
import com.codemybrainsout.imageviewer.view.user.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhotoActivity : AppCompatActivity(), PhotoContract.View, RecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private val binding by viewBinding(ActivityPhotoBinding::inflate)

    private lateinit var photoAdapter: PhotoAdapter

    @Inject
    lateinit var photoPresenter: PhotoPresenter

    var page = 1
    var limit = 20
    var orderBy = "latest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar()

        photoAdapter = PhotoAdapter(this)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        photoAdapter.setRecyclerViewItemClickListener(this)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewPhotos.layoutManager = linearLayoutManager
        binding.recyclerViewPhotos.adapter = photoAdapter
        binding.recyclerViewPhotos.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                photoPresenter.loadMorePhotos(page, limit, orderBy)
            }
        })
        photoPresenter.loadPhotos(page, limit, orderBy)
    }

    override fun showLoading() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        if (binding.swipeRefreshLayout.isRefreshing) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun showUser(user: User?) {
        val intent: Intent = UserActivity.getUserActivityIntent(this, user)
        startActivity(intent)
    }

    override fun showPhoto(photo: Photo?) {
        val intent: Intent = DetailActivity.getDetailActivityIntent(this, photo)
        startActivity(intent)
    }

    override fun refreshPhotos(list: List<Photo>) {
        photoAdapter.setPhotos(list)
    }

    override fun addPhotos(list: List<Photo>) {
        photoAdapter.addPhotos(list)
    }

    override fun showError(s: String?) {
        s?.let { binding.root.showErrorSnackbar(it) }
    }

    fun setToolbar() {
        setSupportActionBar(activityPhotoToolbar)
        val actionBar: ActionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_photo))
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(baseModel: BaseModel?) {
        photoPresenter.openPhoto(baseModel as Photo?)
    }

    override fun onUserClick(user: User?) {
        photoPresenter.openUser(user)
    }

    override fun onRefresh() {
        page = 1
        photoPresenter.loadPhotos(page, limit, orderBy)
    }

    companion object {
        fun getPhotoActivityIntent(context: Context?): Intent {
            return Intent(context, PhotoActivity::class.java)
        }
    }
}