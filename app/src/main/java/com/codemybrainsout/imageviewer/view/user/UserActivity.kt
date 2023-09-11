package com.codemybrainsout.imageviewer.view.user

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.codemybrainsout.imageviewer.R
import com.codemybrainsout.imageviewer.databinding.ActivityUserBinding
import com.codemybrainsout.imageviewer.extensions.addProgressLayout
import com.codemybrainsout.imageviewer.extensions.removeProgressLayout
import com.codemybrainsout.imageviewer.extensions.showErrorSnackbar
import com.codemybrainsout.imageviewer.extensions.viewBinding
import com.codemybrainsout.imageviewer.listener.EndlessRecyclerViewScrollListener
import com.codemybrainsout.imageviewer.listener.RecyclerViewItemClickListener
import com.codemybrainsout.imageviewer.model.BaseModel
import com.codemybrainsout.imageviewer.model.Photo
import com.codemybrainsout.imageviewer.model.User
import com.codemybrainsout.imageviewer.transformation.BlurTransformation
import com.codemybrainsout.imageviewer.view.detail.DetailActivity
import com.codemybrainsout.imageviewer.view.home.MultiViewAdapter
import javax.inject.Inject

class UserActivity : AppCompatActivity(), UserContract.View, RecyclerViewItemClickListener {

    private val binding by viewBinding(ActivityUserBinding::inflate)

    @Inject
    lateinit var userPresenter: UserPresenter

    private val page = 1
    private val limit = 20

    private val multiViewAdapter: MultiViewAdapter by lazy { MultiViewAdapter(this) }
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            multiViewAdapter.setRecyclerViewItemClickListener(this@UserActivity)
            val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, 1)
            recyclerViewPhotos.layoutManager = staggeredGridLayoutManager
            recyclerViewPhotos.adapter = multiViewAdapter
            recyclerViewPhotos.addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    userPresenter.loadMorePhotos(username, page, limit)
                }
            })
        }

        if (intent.hasExtra(EXTRA_USER)) {
            val user = intent.getSerializableExtra(EXTRA_USER) as User
            username = user.username
            userPresenter.loadUser(username)
        }
    }

    private fun beginEnterTransition() {
        binding.imageViewBackground.post {
            val circularReveal: Animator = ViewAnimationUtils.createCircularReveal(
                binding.imageViewBackground,
                binding.imageViewBackground.width / 2,
                0,
                0f,
                binding.imageViewBackground.width.toFloat()
            )
            circularReveal.interpolator = AccelerateInterpolator(1.5f)
            circularReveal.duration = 500
            circularReveal.start()
        }
    }

    override fun showLoading() {
        binding.root.addProgressLayout(this)
    }

    override fun hideLoading() {
        binding.root.removeProgressLayout()
    }

    override fun showUser(user: User) {
        if (!TextUtils.isEmpty(user.profileImage!!.large)) {
            Glide.with(this)
                .load(user.profileImage!!.large)
                .into(binding.imageViewUser)
        }
        if (!TextUtils.isEmpty(user.profileImage!!.large)) {
            Glide.with(this)
                .load(user.profileImage!!.large)
                .transform(BlurTransformation(this, 15))
                .into(binding.imageViewBackground)
        }
        beginEnterTransition()
        binding.collapsingToolbar.isTitleEnabled = true
        binding.collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT)
        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE)
        binding.collapsingToolbar.title = if (TextUtils.isEmpty(user.name)) "" else user.name
        binding.textViewUserName.text = if (TextUtils.isEmpty(user.name)) "" else user.name
        binding.textViewBio.text = if (TextUtils.isEmpty(user.bio)) "" else user.bio
        binding.textViewFollowers.text = "${user.followersCount} ${getString(R.string.followers)}"
        binding.textViewFollowing.text = "${user.followingCount} ${getString(R.string.following)}"
        userPresenter.loadPhotos(user.username, page, limit)
    }

    override fun refreshPhotos(list: List<Photo>) {
        multiViewAdapter.setPhotos(list)
    }

    override fun addPhotos(list: List<Photo>) {
        multiViewAdapter.addPhotos(list)
    }

    override fun showPhotoDetails(photo: Photo?) {
        val intent: Intent = DetailActivity.getDetailActivityIntent(this, photo)
        startActivity(intent)
    }

    override fun showError(s: String?) {
        s?.let { binding.root.showErrorSnackbar(it) }
    }

    override fun onItemClick(baseModel: BaseModel?) {
        userPresenter.openPhotoDetails(baseModel as Photo?)
    }

    override fun onUserClick(user: User?) {}

    fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
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

    companion object {
        private const val EXTRA_USER = "extra_user"
        fun getUserActivityIntent(context: Context?, user: User?): Intent {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(EXTRA_USER, user)
            return intent
        }
    }
}