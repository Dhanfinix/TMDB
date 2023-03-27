package com.dhandev.myapp1.ui.watchlist

import com.dhandev.myapp1.data.source.local.entity.MovieEntity

interface WatchlistDelegate {
    fun onItemClicked(selected: MovieEntity)
    fun onItemDeleted(selected: MovieEntity)
}