package com.codemybrainsout.imageviewer.view.collection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codemybrainsout.imageviewer.databinding.ActivityCollectionBinding
import com.codemybrainsout.imageviewer.extensions.showErrorSnackbar
import com.codemybrainsout.imageviewer.extensions.viewBinding
import com.codemybrainsout.imageviewer.listener.EndlessRecyclerViewScrollListener
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener
import com.codemybrainsout.imageviewer.model.BaseModel
import com.codemybrainsout.imageviewer.model.Collection
import com.codemybrainsout.imageviewer.model.User
import com.codemybrainsout.imageviewer.view.single.SingleCollectionActivity
import com.codemybrainsout.imageviewer.view.user.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CollectionActivity : AppCompatActivity(), CollectionContract.View, RecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private val binding by viewBinding(ActivityCollectionBinding::inflate)

    @Inject
    lateinit var collectionPresenter: CollectionPresenter

    private val collectionAdapter: CollectionAdapter by lazy {
        CollectionAdapter(this)
    }

    private var page = 1
    private val limit = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        collectionAdapter.setRecyclerViewItemClickListener(this)

        binding.apply {
            val linearLayoutManager = LinearLayoutManager(this@CollectionActivity)
            recyclerViewCollection.layoutManager = linearLayoutManager
            recyclerViewCollection.adapter = collectionAdapter
            recyclerViewCollection.addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(linearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    collectionPresenter.loadMoreCollections(page, limit)
                }
            })

            swipeRefreshLayout.setOnRefreshListener(this@CollectionActivity)

            toolbar.setNavigationOnClickListener {
               onBackPressed()
            }
        }

        loadCollections()
    }

    private fun loadCollections() {
        collectionPresenter.loadCollections(page, limit)
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

    override fun showCollection(collection: Collection?) {
        val intent: Intent = SingleCollectionActivity.getSingleCollectionIntent(this, collection)
        startActivity(intent)
    }

    override fun refreshCollections(list: List<Collection>) {
        collectionAdapter.setCollections(list)
    }

    override fun addCollections(list: List<Collection>) {
        collectionAdapter.addCollections(list)
    }

    override fun showError(s: String?) {
        binding.root.showErrorSnackbar(s ?: "")
    }

    override fun onItemClick(baseModel: BaseModel?) {
        collectionPresenter.openCollection(baseModel as Collection?)
    }

    override fun onUserClick(user: User?) {
        collectionPresenter.openUser(user)
    }

    override fun onRefresh() {
        page = 1
        collectionPresenter.loadCollections(page, limit)
    }

    companion object {
        fun getCollectionActivityIntent(context: Context?): Intent {
            return Intent(context, CollectionActivity::class.java)
        }
    }
}