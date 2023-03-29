package com.dhandev.myapp1.ui.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.data.source.remote.response.ResultsItem
import com.dhandev.myapp1.databinding.ActivityDetailBinding
import com.dhandev.myapp1.ui.comment.CommentActivity
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

    private var data : ResultsItem? = null
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

        val isFav = intent.getBooleanExtra(FAVORITE, false)
        data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (isFav){
                dataFav()
            } else {
                intent.getParcelableExtra(DETAIL_INFO, ResultsItem::class.java)
            }
        } else {
            if (isFav){
                val dataFav = intent.getParcelableExtra<MovieEntity>(DETAIL_INFO)
                ResultsItem(dataFav?.overview, dataFav?.originalTitle, dataFav?.title, dataFav?.releaseDate, dataFav?.posterPath, dataFav?.backdropPath, dataFav?.voteAverage, dataFav?.id)
            } else {
                intent.getParcelableExtra(DETAIL_INFO)
            }
        }

        binding.apply {
            skeleton = ivBackdrop.createSkeleton()
            skeletonPoster = ivPoster.createSkeleton()
            skeleton.showSkeleton()
            skeletonPoster.showSkeleton()

            Glide.with(this@DetailActivity)
                .load("https://image.tmdb.org/t/p/original${data?.backdropPath}")
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
                .load("https://image.tmdb.org/t/p/original${data?.posterPath}")
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

            tvOverviewTitle.text = data?.originalTitle
            tvReleaseDate.text = data?.releaseDate
            tvRating.text = getString(R.string.rating, data?.voteAverage.toString())
            tvOverview.text = data?.overview

            viewModel.getById(this@DetailActivity, this@DetailActivity, data?.id!!)

            val detailData = MovieEntity(data?.overview, data?.originalTitle, data?.title, data?.releaseDate, data?.posterPath, data?.backdropPath, data?.voteAverage, data?.id)
            viewModel.movieTvId.observe(this@DetailActivity){ resultItem->
                if(resultItem == null){
                    btnFav.text = getString(R.string.add_to_watch_list)
                    btnFav.setOnClickListener {
                        data?.let { viewModel.insertFav(this@DetailActivity, detailData) }
                        uiUtil.showSnackBar(this@DetailActivity, binding.root, getString(R.string.added_to_watchlist), getString(
                            R.string.undo), Snackbar.LENGTH_LONG){
                            viewModel.delete(this@DetailActivity, detailData)
                        }
                        btnFav.text = getString(R.string.remove_watchlist)
                    }
                } else {
                    btnFav.text = getString(R.string.remove_watchlist)
                    btnFav.setOnClickListener {
                        data?.let { viewModel.delete(this@DetailActivity, detailData) }
                        uiUtil.showSnackBar(this@DetailActivity, binding.root, getString(R.string.removed_from_watch_list), getString(
                            R.string.undo), Snackbar.LENGTH_LONG){
                            viewModel.insertFav(this@DetailActivity, detailData)
                        }
                        btnFav.text = getString(R.string.add_to_watch_list)
                    }
                }
            }

            btnAddReview.setOnClickListener {
                CommentActivity.openNew(this@DetailActivity, data?.id!!)
            }

            viewModel.getCommentById(this@DetailActivity, this@DetailActivity, data?.id!!)
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

        }
    }

    private fun retrySnackBar() {
        uiUtil.showSnackBar(this@DetailActivity, binding.root, getString(R.string.image_failed_load), "Retry", Snackbar.LENGTH_INDEFINITE) {
            val mIntent = intent
            finish()
            startActivity(mIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun dataFav() : ResultsItem{
        val dataFav = intent.getParcelableExtra(DETAIL_INFO, MovieEntity::class.java)
        return ResultsItem(dataFav?.overview, dataFav?.originalTitle, dataFav?.title, dataFav?.releaseDate, dataFav?.posterPath, dataFav?.backdropPath, dataFav?.voteAverage, dataFav?.id)
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
        const val FAVORITE = "favorite"
        const val BEFORE = "before"

        fun open(activity: AppCompatActivity, title: String, activityBefore: String, data: ResultsItem){
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(PAGE_TITLE, title)
            intent.putExtra(BEFORE, activityBefore)
            intent.putExtra(DETAIL_INFO, data)
            ActivityCompat.startActivity(activity, intent, null)
        }

        fun openFavorite(activity: AppCompatActivity, title: String, activityBefore: String, data: MovieEntity, isFavorite: Boolean){
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(PAGE_TITLE, title)
            intent.putExtra(BEFORE, activityBefore)
            intent.putExtra(DETAIL_INFO, data)
            intent.putExtra(FAVORITE, isFavorite)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}