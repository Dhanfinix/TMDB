package com.dhandev.myapp1.ui.favorite

import com.dhandev.myapp1.data.source.local.entity.MovieEntity

interface FavoriteDelegate {
    fun onItemClicked(selected: MovieEntity)
}