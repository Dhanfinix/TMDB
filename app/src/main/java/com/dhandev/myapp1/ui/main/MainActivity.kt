package com.dhandev.myapp1.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhandev.myapp1.ui.LoginActivity
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.data.source.remote.response.ResultsItem
import com.dhandev.myapp1.databinding.ActivityMainBinding
import com.dhandev.myapp1.ui.detail.DetailActivity
import com.dhandev.myapp1.ui.list.ListActivity
import com.dhandev.myapp1.ui.people.PeopleActivity
import com.dhandev.myapp1.ui.watchlist.WatchlistActivity
import com.dhandev.myapp1.ui.watchlist.WatchlistViewModel
import com.dhandev.myapp1.utils.UiUtils


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainWatchListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var sharedPref: SharedPreferences
    private val viewModel: WatchlistViewModel by viewModels()
    private var query = ""
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences(LoginActivity.USER_DATA, MODE_PRIVATE)
        title = getString(R.string.greetings, sharedPref.getString(LoginActivity.NAME, ""))
        adapter = MainWatchListAdapter()
        binding.rvWatchList.adapter = adapter

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvWatchList.layoutManager = linearLayoutManager

        adapter.delegate = object : MainWathcListDelegate {
            override fun onItemClicked(selected: MovieEntity) {
                val dataResult = ResultsItem(selected.overview, selected.originalTitle, selected.title, selected.releaseDate, selected.posterPath, selected.backdropPath, selected.voteAverage, selected.id)
                DetailActivity.open(this@MainActivity, "Movie Detail", getString(R.string.home), selected.type!!,dataResult.id!!)
            }
        }
        viewModel.getFav(this, this)
        viewModel.data.observe(this) {
            if (it.isEmpty()) {
                binding.tvNotFound.visibility = View.VISIBLE
            } else {
                binding.tvNotFound.visibility = View.GONE
            }
            adapter.setAdapter(it.take(5))
        }

        binding.apply {
            searchBar.setOnKeyListener { _, keyEvent, event ->
                query = searchBar.text.trim().toString()
                //need to add check action down too, because if not it will triggered twice (down and up)
                if (keyEvent == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    validateAndGo(query)

                }
                return@setOnKeyListener true
            }

            //drawable end listener
            searchBar.setOnTouchListener(OnTouchListener { _, event ->
                val drawableRight = 2
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= searchBar.right - searchBar.compoundDrawables[drawableRight].bounds.width()
                    ) {
                        query = searchBar.text.trim().toString()
                        validateAndGo(query)
                        return@OnTouchListener true
                    }
                }
                false
            })

            btnMovTop.setOnClickListener {
                ListActivity.open(this@MainActivity, "Top Rated Movies", "movie/top_rated")
            }
            btnMovUpcoming.setOnClickListener {
                ListActivity.open(this@MainActivity, "Upcoming Movies", "movie/upcoming")
            }
            btnMovNow.setOnClickListener {
                ListActivity.open(this@MainActivity, "Now Playing Movies", "movie/now_playing")
            }
            btnMovPopular.setOnClickListener {
                ListActivity.open(this@MainActivity, "Popular Movies", "movie/popular")
            }
            //TV SHOW - STILL USING DATA FROM MOVIE
            btnTvPopular.setOnClickListener {
                ListActivity.open(this@MainActivity, "Popular TV Show", "tv/popular")
            }
            btnTvTop.setOnClickListener {
                ListActivity.open(this@MainActivity, "Top Rated Tv Show", "tv/top_rated")
            }
            btnTvOnair.setOnClickListener {
                ListActivity.open(this@MainActivity, "On Air Tv Show", "tv/on_the_air")
            }
            btnTvAirtoday.setOnClickListener {
                ListActivity.open(this@MainActivity, "Airing Today", "tv/airing_today")
            }
            //PEOPLE - STILL USING DATA FROM MOVIE
            btnPeoplePopular.setOnClickListener {
                PeopleActivity.open(this@MainActivity, "Popular People")
            }
            tvShowAll.setOnClickListener {
                WatchlistActivity.open(this@MainActivity)
            }
        }
    }

    private fun validateAndGo(query: String) {
        if (query.isEmpty() || query == ""){
            Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
        } else {
            ListActivity.openSearch(this@MainActivity, "Result of \"$query\"", "search/movie",
                query
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                UiUtils().showAlert(this, "Logout", "Are you sure to logout?", "Yes", "No",
                {
                    sharedPref.edit().remove(LoginActivity.IS_LOGIN).apply()
                    viewModel.clearDatabase(this)
                    LoginActivity.open(this)
                    finish()
                }, {
                    it.dismiss()
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        fun open(activity: AppCompatActivity){
            val intent = Intent(activity, MainActivity::class.java)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }

}