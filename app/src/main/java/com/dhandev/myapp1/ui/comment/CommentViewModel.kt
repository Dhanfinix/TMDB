package com.dhandev.myapp1.ui.comment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhandev.myapp1.data.source.local.entity.CommentEntity
import com.dhandev.myapp1.data.source.local.room.CommentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentViewModel:ViewModel() {
    fun insertComment(context: Context, data: CommentEntity){
        viewModelScope.launch(Dispatchers.IO) {
            CommentDatabase.getDatabase(context).commentDao().insertComment(data)
        }
    }
}