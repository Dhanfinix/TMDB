package com.dhandev.myapp1.ui.comment

import android.content.Context
import androidx.lifecycle.*
import com.dhandev.myapp1.data.source.local.entity.CommentEntity
import com.dhandev.myapp1.data.source.local.room.CommentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentViewModel:ViewModel() {
    private val _commentById = MutableLiveData<CommentEntity>()
    val commentById : LiveData<CommentEntity>
        get() = _commentById

    fun getCommentById(context: Context, owner: LifecycleOwner, id: Int) {
        CommentDatabase.getDatabase(context).commentDao().getCommentId(id).observe(owner) { result ->
            _commentById.postValue(result)
        }
    }

    fun insertComment(context: Context, data: CommentEntity){
        viewModelScope.launch(Dispatchers.IO) {
            CommentDatabase.getDatabase(context).commentDao().insertComment(data)
        }
    }

    fun deleteComment(context: Context, data: CommentEntity){
        viewModelScope.launch(Dispatchers.IO) {
            CommentDatabase.getDatabase(context).commentDao().deleteComment(data)
        }
    }

    fun updateComment(context: Context, data: CommentEntity){
        viewModelScope.launch(Dispatchers.IO) {
            CommentDatabase.getDatabase(context).commentDao().updateComment(data)
        }
    }
}