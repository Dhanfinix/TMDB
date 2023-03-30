package com.dhandev.myapp1.ui.watchlist

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.data.source.local.entity.WatchlistUpdate
import com.dhandev.myapp1.databinding.ActivityWatchlistBinding
import com.dhandev.myapp1.ui.detail.DetailActivity
import com.dhandev.myapp1.utils.UiUtils
import com.google.android.material.snackbar.Snackbar

class WatchlistActivity : AppCompatActivity() {
    private lateinit var adapter: WatchlistAdapter
    private lateinit var binding: ActivityWatchlistBinding
    private val viewModel: WatchlistViewModel by viewModels()
    private val uiUtil = UiUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchlistBinding.inflate(layoutInflater)

        // Calling the support action bar and setting it to custom
        this.supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        // Displaying the custom layout in the ActionBar
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_action_bar)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvBack = findViewById<TextView>(R.id.tvBack)

        tvTitle.text = getString(R.string.my_watch_list)
        tvBack.text = getString(R.string.home)
        tvBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setContentView(binding.root)

        adapter = WatchlistAdapter()
        binding.rvList.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        adapter.delegate = object : WatchlistDelegate {
            override fun onItemClicked(selected: MovieEntity) {
                DetailActivity.open(this@WatchlistActivity, "Detail", getString(R.string.favorite), selected.type!!, selected.id!!)
            }

            override fun onItemDeleted(selected: MovieEntity) {
                viewModel.updateWatchlist(this@WatchlistActivity, WatchlistUpdate(selected.id, false))
                uiUtil.showSnackBar(this@WatchlistActivity, binding.root, getString(R.string.removed_from_watch_list), getString(
                                    R.string.undo), Snackbar.LENGTH_LONG){
                    viewModel.updateWatchlist(this@WatchlistActivity, WatchlistUpdate(selected.id, true))
                }
            }
        }

        viewModel.getFav(this, this)
        viewModel.data.observe(this) {
            if (it.isEmpty()) {
                binding.notFound.visibility = View.VISIBLE
            } else {
                binding.notFound.visibility = View.GONE
            }
            adapter.setAdapter(it)
        }
    }

    //overide back button on action bar with onBackPressed, because the default swipe to opposite direction
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        return true

    }

    companion object {

        fun open(activity: AppCompatActivity) {
            val intent = Intent(activity, WatchlistActivity::class.java)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}