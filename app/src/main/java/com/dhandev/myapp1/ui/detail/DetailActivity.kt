package com.dhandev.myapp1.ui.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.data.source.remote.response.ResultsItem
import com.dhandev.myapp1.databinding.ActivityDetailBinding
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.createSkeleton


class DetailActivity : AppCompatActivity() {
    private lateinit var skeleton: Skeleton
    private lateinit var skeletonPoster: Skeleton
    private lateinit var binding: ActivityDetailBinding
    private val viewModel : DetailViewModel by viewModels()
//    private lateinit var db : AppDatabase
    private var data : ResultsItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        supportActionBar?.setHomeButtonEnabled(true)
        val isFav = intent.getBooleanExtra(FAVORITE, false)
//        if (isFav){
//            val dataFav = intent.getParcelableExtra(DETAIL_INFO, MovieEntity::class.java)
//
//            data = ResultsItem(dataFav?.overview, dataFav?.originalTitle, dataFav?.title, dataFav?.releaseDate, dataFav?.posterPath, dataFav?.backdropPath, dataFav?.voteAverage, dataFav?.id)
//
//        }
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
        title = intent.getStringExtra(PAGE_TITLE)
        setContentView(binding.root)

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

            data?.let { viewModel.getById(this@DetailActivity, it.id!!) }?.observe(this@DetailActivity){ resultItem->
                if(resultItem == null){
                    btnFav.text = getString(R.string.add_to_watch_list)

                } else {
                    btnFav.text = getString(R.string.remove_watchlist)
                }
            }

            btnFav.setOnClickListener {
                viewModel.getById(this@DetailActivity, data!!.id!!).observe(this@DetailActivity){ movieEntity ->
                    val detailData = MovieEntity(data?.overview, data?.originalTitle, data?.title, data?.releaseDate, data?.posterPath, data?.backdropPath, data?.voteAverage, data?.id)
                    if (movieEntity==null){
                        Toast.makeText(this@DetailActivity, getString(R.string.added_to_favorite), Toast.LENGTH_SHORT).show()
                        data?.let { viewModel.insertFav(this@DetailActivity, detailData) }
                        btnFav.text = getString(R.string.remove_watchlist)
                    } else {
                        data?.let { viewModel.delete(this@DetailActivity, detailData) }
                        Toast.makeText(this@DetailActivity, getString(R.string.removed_from_fav), Toast.LENGTH_SHORT).show()
                        btnFav.text = getString(R.string.add_to_watch_list)
                    }
                }
            }

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

        fun open(activity: AppCompatActivity, title: String, data: ResultsItem){
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(PAGE_TITLE, title)
            intent.putExtra(DETAIL_INFO, data)
            ActivityCompat.startActivity(activity, intent, null)
        }

        fun openFavorite(activity: AppCompatActivity, title: String, data: MovieEntity, isFavorite: Boolean){
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(PAGE_TITLE, title)
            intent.putExtra(DETAIL_INFO, data)
            intent.putExtra(FAVORITE, isFavorite)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}