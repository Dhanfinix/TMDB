package com.dhandev.myapp1.ui.list

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.remote.response.ResultsItem
import com.dhandev.myapp1.databinding.ActivityListBinding
import com.dhandev.myapp1.ui.detail.DetailActivity
import com.dhandev.myapp1.ui.factory.movie.NowPlayingMovieModelFactory
import com.dhandev.myapp1.ui.factory.movie.PopularMovieModelFactory
import com.dhandev.myapp1.ui.factory.movie.TopRatedMovieModelFactory
import com.dhandev.myapp1.ui.factory.movie.UpcomingMovieModelFactory
import com.dhandev.myapp1.ui.factory.tv.AirTodayTvModelFactory
import com.dhandev.myapp1.ui.factory.tv.OnAirTvModelFactory
import com.dhandev.myapp1.ui.factory.tv.PopularTvModelFactory
import com.dhandev.myapp1.ui.factory.tv.TopRatedTvModelFactory
import com.dhandev.myapp1.utils.TypeEnum
import com.dhandev.myapp1.utils.UiUtils
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton

class ListActivity : AppCompatActivity() {
    private lateinit var adapter: MovieListAdapter
    private lateinit var binding: ActivityListBinding
    private lateinit var skeleton: Skeleton
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var path = ""
    private var query = ""
    private var type = ""
    private lateinit var loading : Dialog
    private lateinit var viewModel : ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        // Calling the support action bar and setting it to custom
        this.supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        // Displaying the custom layout in the ActionBar
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_action_bar)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvBack = findViewById<TextView>(R.id.tvBack)

        tvTitle.text = intent.getStringExtra(LIST_TITLE)
        tvBack.text = getString(R.string.home)
        tvBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setContentView(binding.root)

        //get path for endpoint
        path = intent.getStringExtra(FETCH_PATH) ?: "movie/top_rated"
        type = if (path.contains("movie")) TypeEnum.MOVIE.body else TypeEnum.TV.body
        query = intent.getStringExtra(QUERY) ?: ""

        adapter = MovieListAdapter()
        binding.rvList.adapter = adapter

        linearLayoutManager = LinearLayoutManager(this, VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        adapter.delegate = object : MovieDelegate {
            override fun onItemClicked(selected: ResultsItem) {
                DetailActivity.open(this@ListActivity, "Detail", intent.getStringExtra(LIST_TITLE) ?: "List", type, selected.id!!)
            }

        }
        skeleton = binding.rvList.applySkeleton(R.layout.list_row_item, 6)


        //get data from API
        getData()

        binding.swipeToRefresh.setOnRefreshListener {
            //disable inherited loading of swipe layout
            binding.swipeToRefresh.isRefreshing = false
            //get data from API
            getData()
        }

    }

    private fun getData() {
        //TODO: CHANGE FACTORY BASED ON PATH/ TYPE

        val factory = when (path) {
            "movie/top_rated" -> TopRatedMovieModelFactory.getInstance(this)
            "movie/popular" -> PopularMovieModelFactory.getInstance(this)
            "movie/upcoming" -> UpcomingMovieModelFactory.getInstance(this)
            "movie/now_playing" -> NowPlayingMovieModelFactory.getInstance(this)
            "tv/top_rated" -> TopRatedTvModelFactory.getInstance(this)
            "tv/popular" -> PopularTvModelFactory.getInstance(this)
            "tv/airing_today" -> AirTodayTvModelFactory.getInstance(this)
            "tv/on_the_air" -> OnAirTvModelFactory.getInstance(this)
            else -> throw IllegalArgumentException("Invalid endpoint")
        }
        viewModel = ViewModelProvider(this, factory)[ListViewModel::class.java]

        //show shimmering/skeleton and loading popup
        skeleton.showSkeleton()
        loading = UiUtils().showLoading(this)

        //get data by calling it from view model, pass path(endpoint), query(for search),
        // and add callback and error
        viewModel.errorMsg.observe(this){
            loading.dismiss()
            showAlert(it)
        }
        //observe fetched data from previous function
        viewModel.movieTvData.observe(this){movieData->
            adapter.setAdapter(movieData)
            binding.rvList.isVisible = movieData.isNotEmpty()
            skeleton.showOriginal()
            loading.dismiss()
            if (movieData.isEmpty()){
                binding.notFound.visibility = View.VISIBLE
            }
        }


    }

    private fun showAlert(message: String) {
        //show alert by creating showAlert instance from UiUtils, pass needed parameters, and add callback
        //for positive and negative button
        UiUtils().showAlert(this, "Warning", message, "Retry", "Back",
            {
                getData()
            }, {
                onBackPressedDispatcher.onBackPressed()
            })
    }

    //overide back button on action bar with onBackPressed, because the default swipe to opposite direction
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == android.R.id.home){
            onBackPressedDispatcher.onBackPressed()
        }
        return true

    }

    companion object{
        const val LIST_TITLE = "list_title"
        const val FETCH_PATH = "fetch_path"
        const val QUERY = "query"

        fun open(activity: AppCompatActivity, title: String, path: String){
            val intent = Intent(activity, ListActivity::class.java)
            intent.putExtra(LIST_TITLE, title)
            intent.putExtra(FETCH_PATH, path)
            ActivityCompat.startActivity(activity, intent, null)
        }

        fun openSearch(activity: AppCompatActivity, title: String, path: String, query: String){
            val intent = Intent(activity, ListActivity::class.java)
            intent.putExtra(LIST_TITLE, title)
            intent.putExtra(FETCH_PATH, path)
            intent.putExtra(QUERY, query)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}