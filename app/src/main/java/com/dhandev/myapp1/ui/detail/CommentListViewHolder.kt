package com.dhandev.myapp1.ui.detail

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dhandev.myapp1.R

class CommentListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvName: TextView = itemView.findViewById(R.id.tv_comment_name)
    val tvBody: TextView = itemView.findViewById(R.id.tv_comment_body)
}