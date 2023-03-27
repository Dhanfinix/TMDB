package com.dhandev.myapp1.ui.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.dhandev.myapp1.BuildConfig
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.remote.network.ApiConfig
import com.dhandev.myapp1.data.source.remote.response.MovieTvResponse
import com.dhandev.myapp1.data.source.remote.response.ResultsItem
import com.dhandev.myapp1.databinding.ActivityListBinding
import com.dhandev.myapp1.ui.detail.DetailActivity
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListActivity : AppCompatActivity() {
    private lateinit var adapter: MovieListAdapter
    private lateinit var binding: ActivityListBinding
    private lateinit var skeleton: Skeleton
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var path = ""
    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        supportActionBar?.setHomeButtonEnabled(true)
        title = intent.getStringExtra(LIST_TITLE)
        setContentView(binding.root)

        //get path for endpoint
        path = intent.getStringExtra(FETCH_PATH) ?: "movie/top_rated"
        query = intent.getStringExtra(QUERY) ?: "A"

        adapter = MovieListAdapter()
        binding.rvList.adapter = adapter

        linearLayoutManager = LinearLayoutManager(this, VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        adapter.delegate = object : MovieDelegate {
            override fun onItemClicked(selected: ResultsItem) {
                DetailActivity.open(this@ListActivity, "Detail", selected)
            }

        }
        skeleton = binding.rvList.applySkeleton(R.layout.list_row_item, 6)

        skeleton.showSkeleton()
        ApiConfig.getApiService()
            .getMovies(path, BuildConfig.API_KEY, "en-US", 1, query)
            .enqueue(object : Callback<MovieTvResponse> {
                override fun onResponse(
                    call: Call<MovieTvResponse>,
                    response: Response<MovieTvResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val movieData = response.body()!!.results!!
                        adapter.setAdapter(movieData)
                        binding.rvList.isVisible = movieData.isNotEmpty()
                        skeleton.showOriginal()
                        if (movieData.isEmpty()){
                            binding.notFound.visibility = View.VISIBLE
                        }
                    } else {
                        Log.e("TAG", "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<MovieTvResponse>, t: Throwable) {
                    Log.d("Failure", t.message!!)
                }
            })


    }


//
//    private fun showDataFav() {
//
//    }

    //overide back button pada action bar dengan onBackPressed, karena defaultnya seperti merestart activity
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