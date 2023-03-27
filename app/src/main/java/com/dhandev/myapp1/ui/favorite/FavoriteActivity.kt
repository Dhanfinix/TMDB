package com.dhandev.myapp1.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.databinding.ActivityFavoriteBinding
import com.dhandev.myapp1.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteListAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Favorite"

        adapter = FavoriteListAdapter()
        binding.rvList.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        adapter.delegate = object : FavoriteDelegate {
            override fun onItemClicked(selected: MovieEntity) {
                DetailActivity.openFavorite(this@FavoriteActivity, "Detail", selected, true)
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
            val intent = Intent(activity, FavoriteActivity::class.java)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}