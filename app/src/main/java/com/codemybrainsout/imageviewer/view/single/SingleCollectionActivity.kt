package com.codemybrainsout.imageviewer.view.single

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codemybrainsout.imageviewer.R
import com.codemybrainsout.imageviewer.databinding.ActivitySingleCollectionBinding
import com.codemybrainsout.imageviewer.extensions.showErrorSnackbar
import com.codemybrainsout.imageviewer.extensions.viewBinding
import com.codemybrainsout.imageviewer.listener.EndlessRecyclerViewScrollListener
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener
import com.codemybrainsout.imageviewer.model.BaseModel
import com.codemybrainsout.imageviewer.model.Collection
import com.codemybrainsout.imageviewer.model.Photo
import com.codemybrainsout.imageviewer.model.User
import com.codemybrainsout.imageviewer.view.detail.DetailActivity
import com.codemybrainsout.imageviewer.view.photo.PhotoAdapter
import com.codemybrainsout.imageviewer.view.user.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SingleCollectionActivity : AppCompatActivity(), SingleContract.View, RecyclerViewItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private val binding by viewBinding(ActivitySingleCollectionBinding::inflate)

    @Inject
    lateinit var singlePresenter: SinglePresenter

    private var page = 1
    private val limit = 20
    private var collection: Collection? = null

    private val photoAdapter: PhotoAdapter by lazy { PhotoAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(EXTRA_COLLECTION)) {
            collection = intent.getParcelableExtra(EXTRA_COLLECTION)
            singlePresenter.loadCollection(collection)
            collection?.id?.let {
                singlePresenter.loadPhotos(it, page, limit)
            }
        }

        binding.apply {
            swipeRefreshLayout.setOnRefreshListener(this@SingleCollectionActivity)
            photoAdapter.setRecyclerViewItemClickListener(this@SingleCollectionActivity)
            val linearLayoutManager = LinearLayoutManager(this@SingleCollectionActivity)
            recyclerViewSingleCollection.layoutManager = linearLayoutManager
            recyclerViewSingleCollection.adapter = photoAdapter
            recyclerViewSingleCollection.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    collection?.id?.let {
                        singlePresenter.loadMorePhotos(it, page, limit)
                    }
                }
            })
        }
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

    override fun showCollectionDetails(collection: Collection?) {
        supportActionBar?.setTitle(collection!!.title)
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
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(baseModel: BaseModel?) {
        singlePresenter.openPhoto(baseModel as Photo?)
    }

    override fun onUserClick(user: User?) {
        singlePresenter.openUser(user)
    }

    override fun onRefresh() {
        page = 1
        collection?.id?.let { singlePresenter.loadPhotos(it, page, limit) }
    }

    companion object {
        private const val EXTRA_COLLECTION = "extra_collection"
        fun getSingleCollectionIntent(context: Context?, collection: Collection?): Intent {
            val intent = Intent(context, SingleCollectionActivity::class.java)
            intent.putExtra(EXTRA_COLLECTION, collection)
            return intent
        }
    }
}