package com.dhandev.myapp1.ui.people

import com.dhandev.myapp1.data.source.remote.response.ResultsPeopleItem

interface PeopleDelegate {
    fun onItemClicked(selected: ResultsPeopleItem)
}