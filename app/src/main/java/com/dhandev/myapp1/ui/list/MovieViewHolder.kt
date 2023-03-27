package com.dhandev.myapp1.ui.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dhandev.myapp1.R

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivPoster: ImageView = itemView.findViewById(R.id.iv_movie_poster)
    val tvTitle: TextView = itemView.findViewById(R.id.tv_movie_title)
    val tvOverview: TextView = itemView.findViewById(R.id.tv_movie_overview)
}