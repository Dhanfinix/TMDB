package com.dhandev.myapp1.ui.list

import com.dhandev.myapp1.data.source.remote.response.ResultsItem

interface MovieDelegate {
    fun onItemClicked(selected: ResultsItem)
}