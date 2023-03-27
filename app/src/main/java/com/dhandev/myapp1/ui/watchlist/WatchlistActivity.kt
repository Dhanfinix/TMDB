package com.dhandev.myapp1.ui.watchlist

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.databinding.ActivityFavoriteBinding
import com.dhandev.myapp1.ui.detail.DetailActivity

class WatchlistActivity : AppCompatActivity() {
    private lateinit var adapter: WatchlistAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel: WatchlistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.my_watch_list)

        adapter = WatchlistAdapter()
        binding.rvList.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        adapter.delegate = object : WatchlistDelegate {
            override fun onItemClicked(selected: MovieEntity) {
                DetailActivity.openFavorite(this@WatchlistActivity, "Detail", selected, true)
            }

            override fun onItemDeleted(selected: MovieEntity) {
                viewModel.delete(this@WatchlistActivity, selected)
                viewModel.getFav(this@WatchlistActivity)
            }
        }

        viewModel.getFav(this)
        viewModel.data.observe(this) {
            if (it.isEmpty()) {
                binding.notFound.visibility = View.VISIBLE
            } else {
                binding.notFound.visibility = View.GONE
            }
            adapter.setAdapter(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFav(this)
    }

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