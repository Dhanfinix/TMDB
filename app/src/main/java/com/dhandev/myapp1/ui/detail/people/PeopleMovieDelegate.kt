package com.dhandev.myapp1.ui.detail.people

import com.dhandev.myapp1.data.source.remote.response.KnownForItem

interface PeopleMovieDelegate {
    fun onItemClicked(selected: KnownForItem)
}