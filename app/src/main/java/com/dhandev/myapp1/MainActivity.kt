package com.dhandev.myapp1

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.dhandev.myapp1.data.source.local.room.AppDatabase
import com.dhandev.myapp1.databinding.ActivityMainBinding
import com.dhandev.myapp1.ui.favorite.FavoriteActivity
import com.dhandev.myapp1.ui.list.ListActivity
import com.dhandev.myapp1.ui.people.PeopleActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences(LoginActivity.USER_DATA, MODE_PRIVATE)

        binding.apply {
            val query = searchBar.text
            searchBar.setOnKeyListener { _, keyEvent, event ->
                //need to add check action down too, because if not it will triggered twice (down and up)
                if (keyEvent == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    if (query.isEmpty() || query.toString() == ""){
                        Toast.makeText(this@MainActivity, "Please input keyword", Toast.LENGTH_SHORT).show()
                    } else {
                        ListActivity.openSearch(this@MainActivity, "Result of \"$query\"", "search/movie", query.toString())
                    }
                }
                return@setOnKeyListener true
            }

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
                showAlertLogout()
            }
            R.id.favorite -> {
                FavoriteActivity.open(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertLogout() {
        // Create an alert builder
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure to logout?")

        // add a button
        builder.setPositiveButton("Yes") { _, _ ->
            sharedPref.edit().clear().apply()
            lifecycleScope.launch(Dispatchers.IO) {
                AppDatabase.getDatabase(this@MainActivity).clearAllTables()
            }
            LoginActivity.open(this)
            finish()
        }

        // create and show the alert dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    companion object{
        fun open(activity: AppCompatActivity){
            val intent = Intent(activity, MainActivity::class.java)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }

}