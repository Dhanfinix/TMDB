package com.dhandev.myapp1.ui.main

import com.dhandev.myapp1.data.source.local.entity.MovieEntity

interface MainWathcListDelegate {
    fun onItemClicked(selected: MovieEntity)
}