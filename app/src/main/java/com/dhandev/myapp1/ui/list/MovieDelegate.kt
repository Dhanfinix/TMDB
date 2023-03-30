package com.dhandev.myapp1.ui.list

import com.dhandev.myapp1.data.source.local.entity.MovieEntity

interface MovieDelegate {
    fun onItemClicked(selected: MovieEntity)
}