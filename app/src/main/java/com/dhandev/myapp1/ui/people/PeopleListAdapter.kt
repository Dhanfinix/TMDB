package com.dhandev.myapp1.ui.people

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.remote.response.ResultsPeopleItem

class PeopleListAdapter: RecyclerView.Adapter<PeopleViewHolder>() {

    private val list: MutableList<ResultsPeopleItem> = mutableListOf()
    var delegate: PeopleDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row_people_item, parent, false)
        return PeopleViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/original/${list[position].profilePath}")
            .placeholder(R.drawable.ic_baseline_image_24)
            .transform(RoundedCorners(16))
            .into(holder.ivPoster)
        holder.tvTitle.text = list[position].name
        holder.itemView.setOnClickListener {
            list[position]?.let { it1 -> delegate?.onItemClicked(it1) }
        }
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(people: List<ResultsPeopleItem>) {
        list.clear()
        list.addAll(people)
        notifyDataSetChanged()
    }

}