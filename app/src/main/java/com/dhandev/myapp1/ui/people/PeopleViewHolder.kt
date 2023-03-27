package com.dhandev.myapp1.ui.people

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dhandev.myapp1.R

class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivPoster: ImageView = itemView.findViewById(R.id.iv_people_poster)
    val tvTitle: TextView = itemView.findViewById(R.id.tv_people_name)
}