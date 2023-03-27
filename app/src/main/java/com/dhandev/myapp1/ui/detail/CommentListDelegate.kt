package com.dhandev.myapp1.ui.detail

import com.dhandev.myapp1.data.source.local.entity.CommentEntity

interface CommentListDelegate {
    fun onItemClicked(selected: CommentEntity)
}