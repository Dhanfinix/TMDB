package com.dhandev.myapp1.ui.detail

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.local.entity.CommentEntity
import com.dhandev.myapp1.data.source.local.entity.WatchlistUpdate
import com.dhandev.myapp1.databinding.ActivityDetailBinding
import com.dhandev.myapp1.ui.comment.CommentActivity
import com.dhandev.myapp1.ui.watchlist.WatchlistActivity
import com.dhandev.myapp1.utils.UiUtils
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.createSkeleton
import com.google.android.material.snackbar.Snackbar


class DetailActivity : AppCompatActivity() {
    private lateinit var skeleton: Skeleton
    private lateinit var skeletonPoster: Skeleton
    private lateinit var binding: ActivityDetailBinding
    private lateinit var adapter : DetailCommentListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel : DetailViewModel by viewModels()
    private val uiUtil = UiUtils()
    private var id = 0
    private var path : String? = null
    private lateinit var loading : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        // Calling the support action bar and setting it to custom
        this.supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        // Displaying the custom layout in the ActionBar
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_action_bar)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvBack = findViewById<TextView>(R.id.tvBack)

        tvTitle.text = intent.getStringExtra(PAGE_TITLE)
        tvBack.text = intent.getStringExtra(BEFORE)
        tvBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setContentView(binding.root)

        adapter = DetailCommentListAdapter()
        binding.rvComment.adapter = adapter

        linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvComment.layoutManager = linearLayoutManager

        adapter.delegate = object : CommentListDelegate {
            override fun onItemClicked(selected: CommentEntity) {
                CommentActivity.openToUpdate(this@DetailActivity, selected)
            }
        }

        id = intent.getIntExtra(DETAIL_INFO, 0)
        path = intent.getStringExtra(TYPE)
        getData(id, path)

        binding.swipeToRefresh.setOnRefreshListener {
            //disable inherited loading of swipe layout
            binding.swipeToRefresh.isRefreshing = false
            //get data from API
            getData(id, path)
        }

    }

    private fun getData(id: Int, path: String?) {
        loading = UiUtils().showLoading(this)
        skeleton = binding.ivBackdrop.createSkeleton()
        skeletonPoster = binding.ivPoster.createSkeleton()
        skeleton.showSkeleton()
        skeletonPoster.showSkeleton()

        viewModel.getData("$path/$id"){
            loading.dismiss()
            showAlert(it)
        }

        viewModel.movieTvData.observe(this){currentData->
            binding.apply {
                Glide.with(this@DetailActivity)
                    .load("https://image.tmdb.org/t/p/original${currentData?.backdropPath}")
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            retrySnackBar()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            skeleton.showOriginal()
                            return false
                        }

                    })
                    .into(ivBackdrop)
                Glide.with(this@DetailActivity)
                    .load("https://image.tmdb.org/t/p/original${currentData?.posterPath}")
                    .transform(RoundedCorners(16))
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            retrySnackBar()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            skeletonPoster.showOriginal()
                            return false
                        }

                    })
                    .into(ivPoster)

                tvOverviewTitle.text = currentData?.originalTitle
                tvReleaseDate.text = currentData?.releaseDate
                tvRating.text = getString(R.string.rating, currentData?.voteAverage.toString())
                tvOverview.text = currentData?.overview

                viewModel.getWatchlist(this@DetailActivity, id)

                viewModel.watchlisted.observe(this@DetailActivity){ isWatchlisted->
                    if(!isWatchlisted){
                        btnFav.text = getString(R.string.add_to_watch_list)
                        btnFav.setOnClickListener {
                            it?.let { viewModel.updateWatchlist(this@DetailActivity, WatchlistUpdate(currentData.id, true)) }
                            uiUtil.showSnackBar(this@DetailActivity, binding.root, getString(R.string.added_to_watchlist), getString(
                                R.string.show_all), Snackbar.LENGTH_LONG){
                                WatchlistActivity.open(this@DetailActivity)
                            }
                            btnFav.text = getString(R.string.remove_watchlist)
                        }
                    } else {
                        btnFav.text = getString(R.string.remove_watchlist)
                        btnFav.setOnClickListener {
                            it?.let { viewModel.updateWatchlist(this@DetailActivity, WatchlistUpdate(currentData.id, false)) }
                            uiUtil.showSnackBar(this@DetailActivity, binding.root, getString(R.string.removed_from_watch_list), getString(
                                R.string.undo), Snackbar.LENGTH_LONG){
                                viewModel.updateWatchlist(this@DetailActivity, WatchlistUpdate(currentData.id, true))
                            }
                            btnFav.text = getString(R.string.add_to_watch_list)
                        }
                    }
                }

                btnAddReview.setOnClickListener {
                    CommentActivity.openNew(this@DetailActivity, id)
                }

                viewModel.getCommentById(this@DetailActivity, this@DetailActivity, id)
                viewModel.commentById.observe(this@DetailActivity
                ) { result ->
                    if(result == null || result.isEmpty()){
                        noCommentsFound.text = getString(R.string.no_comments_available)
                        noCommentsFound.visibility = View.VISIBLE
                        binding.rvComment.isVisible = false
                    } else {
                        noCommentsFound.text = getString(R.string.lorem)
                        noCommentsFound.visibility = View.GONE
                        adapter.setAdapter(result)
                        binding.rvComment.isVisible = result.isNotEmpty()
                    }
                }
                loading.dismiss()
            }
        }

    }

    private fun retrySnackBar() {
        uiUtil.showSnackBar(this@DetailActivity, binding.root, getString(R.string.image_failed_load), "Retry", Snackbar.LENGTH_INDEFINITE) {
            val mIntent = intent
            finish()
            startActivity(mIntent)
        }
    }

    private fun showAlert(message: String) {
        //show alert by creating showAlert instance from UiUtils, pass needed parameters, and add callback
        //for positive and negative button
        UiUtils().showAlert(this, "Warning", message, "Retry", "Back",
            {
                skeleton.showOriginal()
                skeletonPoster.showOriginal()
                getData(id, path)
            }, {
                onBackPressedDispatcher.onBackPressed()
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == android.R.id.home){
            onBackPressedDispatcher.onBackPressed()
        }
        return true

    }
    companion object{
        const val PAGE_TITLE = "page_title"
        const val DETAIL_INFO = "detail_info"
        const val BEFORE = "before"
        const val TYPE = "type"

        fun open(activity: AppCompatActivity, title: String, activityBefore: String, type: String, dataId: Int){
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(PAGE_TITLE, title)
            intent.putExtra(BEFORE, activityBefore)
            intent.putExtra(TYPE, type)
            intent.putExtra(DETAIL_INFO, dataId)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}