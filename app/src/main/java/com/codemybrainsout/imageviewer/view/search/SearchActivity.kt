package com.codemybrainsout.imageviewer.view.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codemybrainsout.imageviewer.R
import com.codemybrainsout.imageviewer.databinding.ActivityPhotoBinding
import com.codemybrainsout.imageviewer.databinding.ActivitySearchBinding
import com.codemybrainsout.imageviewer.extensions.showErrorSnackbar
import com.codemybrainsout.imageviewer.extensions.viewBinding
import com.codemybrainsout.imageviewer.listener.EndlessRecyclerViewScrollListener
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener
import com.codemybrainsout.imageviewer.model.BaseModel
import com.codemybrainsout.imageviewer.model.Photo
import com.codemybrainsout.imageviewer.model.User
import com.codemybrainsout.imageviewer.utility.PrefHelper
import com.codemybrainsout.imageviewer.view.detail.DetailActivity
import com.codemybrainsout.imageviewer.view.home.MultiViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), SearchContract.View, RecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var searchPresenter: SearchPresenter

    private val binding by viewBinding(ActivitySearchBinding::inflate)

    private val multiViewAdapter: MultiViewAdapter by lazy { MultiViewAdapter() }

    private var lastSearches: List<String>? = null
    private var page = 1
    private val limit = 20

    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            swipeRefreshLayout.isEnabled = false
            swipeRefreshLayout.setOnRefreshListener(this@SearchActivity)
            multiViewAdapter.setRecyclerViewItemClickListener(this@SearchActivity)
            val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, 1)
            recyclerViewSearch.layoutManager = staggeredGridLayoutManager
            recyclerViewSearch.adapter = multiViewAdapter
            recyclerViewSearch.addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    searchPresenter.searchMorePhotos(query, page, limit)
                }
            })
            //binding.activitySearchBar.setNavButtonEnabled(true)
            //binding.activitySearchBar.setNavigationIcon(R.drawable.ic_arrow_back)
            //binding.activitySearchBar.setOnSearchActionListener(this)
            lastSearches = loadSearchSuggestionFromDisk()
            //activitySearchBar.setLastSuggestions(lastSearches)
            activitySearchBar.performClick()
        }
    }

    private fun loadSearchSuggestionFromDisk(): List<String> {
        return PrefHelper.loadList(this, SEARCH)
    }

    private fun saveSearchSuggestionToDisk(lastSuggestions: List<String>) {
        PrefHelper.storeList(this, SEARCH, lastSuggestions)
    }

    override fun onDestroy() {
        super.onDestroy()
        //save last queries to disk
        //saveSearchSuggestionToDisk(binding.activitySearchBar.getLastSuggestions())
    }

    override fun showLoading() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        if (binding.swipeRefreshLayout.isRefreshing) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun showUser(user: User?) {}
    override fun showPhoto(photo: Photo?) {
        val intent: Intent = DetailActivity.getDetailActivityIntent(this, photo)
        startActivity(intent)
    }

    override fun refreshPhotos(list: List<Photo>) {
        if (list.isNotEmpty()) {
            binding.swipeRefreshLayout.isEnabled = true
        }
        multiViewAdapter.setPhotos(list)
    }

    override fun addPhotos(list: List<Photo>) {
        multiViewAdapter.addPhotos(list)
    }

    override fun showError(s: String?) {
        s?.let { binding.root.showErrorSnackbar(it) }
    }

    fun onSearchStateChanged(enabled: Boolean) {
        val s = if (enabled) "enabled" else "disabled"
        Timber.i("Search $s")
    }

    fun onSearchConfirmed(text: CharSequence) {
        query = text.toString()
        searchPresenter.searchPhotos(text.toString(), page, limit)
    }

    override fun onItemClick(baseModel: BaseModel?) {
        searchPresenter.openPhoto(baseModel as Photo?)
    }

    override fun onUserClick(user: User?) {
        searchPresenter.openUser(user)
    }

    override fun onRefresh() {
        page = 1
        searchPresenter.searchPhotos(query, page, limit)
    }

    companion object {
        private const val SEARCH = "search"
        fun getSearchIntent(context: Context?): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }
}