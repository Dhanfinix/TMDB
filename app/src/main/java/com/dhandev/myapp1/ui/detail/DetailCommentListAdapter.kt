package com.dhandev.myapp1.ui.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.local.entity.CommentEntity

class DetailCommentListAdapter: RecyclerView.Adapter<CommentListViewHolder>() {

    private val list: MutableList<CommentEntity> = mutableListOf()
    var delegate: CommentListDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row_item_comment, parent, false)
        return CommentListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentListViewHolder, position: Int) {
        holder.tvName.text = list[position].username
        holder.tvBody.text = list[position].commentBody
        holder.itemView.setOnClickListener {
            list[position]?.let { it1 -> delegate?.onItemClicked(it1) }
        }
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(comment: List<CommentEntity>) {
        list.clear()
        list.addAll(comment)
        notifyDataSetChanged()
    }

}