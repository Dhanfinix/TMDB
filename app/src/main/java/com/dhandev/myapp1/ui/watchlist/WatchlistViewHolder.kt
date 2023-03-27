package com.dhandev.myapp1.ui.watchlist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dhandev.myapp1.R

class WatchlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivPoster: ImageView = itemView.findViewById(R.id.iv_movie_poster)
    val tvTitle: TextView = itemView.findViewById(R.id.tv_movie_title)
    val btnDelete: ImageView = itemView.findViewById(R.id.btn_delete)
}