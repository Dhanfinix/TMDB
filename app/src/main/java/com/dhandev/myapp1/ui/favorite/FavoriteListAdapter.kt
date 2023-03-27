package com.dhandev.myapp1.ui.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.ui.list.MovieViewHolder

class FavoriteListAdapter: RecyclerView.Adapter<MovieViewHolder>() {

    private val list: MutableList<MovieEntity> = mutableListOf()
    var delegate: FavoriteDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/original/${list[position].posterPath}")
            .placeholder(R.drawable.ic_baseline_image_24)
            .transform(RoundedCorners(16))
            .into(holder.ivPoster)
        holder.tvTitle.text = list[position].originalTitle
        holder.tvOverview.text = list[position].overview
        holder.itemView.setOnClickListener {
            list[position]?.let { it1 -> delegate?.onItemClicked(it1) }
        }
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(movies: List<MovieEntity>) {
        list.clear()
        list.addAll(movies)
        notifyDataSetChanged()
    }

}