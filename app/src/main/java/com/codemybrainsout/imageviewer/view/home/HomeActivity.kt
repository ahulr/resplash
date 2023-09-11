package com.codemybrainsout.imageviewer.view.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codemybrainsout.imageviewer.databinding.ActivityHomeBinding
import com.codemybrainsout.imageviewer.extensions.addProgressLayout
import com.codemybrainsout.imageviewer.extensions.removeProgressLayout
import com.codemybrainsout.imageviewer.extensions.showErrorSnackbar
import com.codemybrainsout.imageviewer.extensions.viewBinding
import com.codemybrainsout.imageviewer.listener.FooterItemClickListener
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener
import com.codemybrainsout.imageviewer.model.*
import com.codemybrainsout.imageviewer.model.Collection
import com.codemybrainsout.imageviewer.view.collection.CollectionActivity
import com.codemybrainsout.imageviewer.view.detail.DetailActivity
import com.codemybrainsout.imageviewer.view.photo.PhotoActivity
import com.codemybrainsout.imageviewer.view.search.SearchActivity
import com.codemybrainsout.imageviewer.view.single.SingleCollectionActivity
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeContract.View, RecyclerViewItemClickListener, FooterItemClickListener {

    private val binding by viewBinding(ActivityHomeBinding::inflate)

    @Inject
    lateinit var multiViewAdapter: MultiViewAdapter

    @Inject
    lateinit var homePresenter: HomePresenter

    var limit = 6
    private var orderBy = "latest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        multiViewAdapter.setRecyclerViewItemClickListener(this)
        multiViewAdapter.setFooterItemClickListener(this)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, 1)
        binding.recyclerViewHome.layoutManager = staggeredGridLayoutManager
        binding.recyclerViewHome.adapter = multiViewAdapter
        homePresenter.loadPhotos(limit, orderBy)
        binding.cardViewSearch.setOnClickListener { openSearch() }
    }

    override fun showLoading() {
        binding.root.addProgressLayout(this)
    }

    override fun hideLoading() {
        binding.root.removeProgressLayout()
    }

    override fun setPhotos(list: List<Photo>) {
        homePresenter.loadCollections(limit)
        multiViewAdapter.addPhotos(list)
        multiViewAdapter.setFooter(Footer.Type.Photo)
    }

    override fun setCollections(list: List<Collection>) {
        multiViewAdapter.addCollections(list)
        multiViewAdapter.setFooter(Footer.Type.Collection)
    }

    override fun openAllCollections() {
        val intent: Intent = CollectionActivity.getCollectionActivityIntent(this)
        startActivity(intent)
    }

    override fun openAllPhotos() {
        val intent: Intent = PhotoActivity.getPhotoActivityIntent(this)
        startActivity(intent)
    }

    override fun showError(s: String?) {
        binding.root.showErrorSnackbar(s ?: "")
    }

    override fun showPhoto(photo: Photo?) {
        val intent: Intent = DetailActivity.getDetailActivityIntent(this, photo)
        startActivity(intent)
    }

    override fun showCollection(collection: Collection?) {
        val intent: Intent =
            SingleCollectionActivity.getSingleCollectionIntent(this, collection)
        startActivity(intent)
    }

    override fun onFooterClick(footer: Footer?) {
        val type = footer?.type
        if (type === Footer.Type.Collection) {
            homePresenter.viewAllCollections()
        } else {
            homePresenter.viewAllPhotos()
        }
    }

    override fun onItemClick(baseModel: BaseModel?) {
        if (baseModel is Photo) {
            homePresenter.openPhoto(baseModel as Photo?)
        } else if (baseModel is Collection) {
            homePresenter.openCollection(baseModel as Collection?)
        }
    }

    override fun onUserClick(user: User?) {}

    fun setToolbar() {}

    private fun openSearch() {
        val intent: Intent = SearchActivity.getSearchIntent(this)
        startActivity(intent)
    }
}