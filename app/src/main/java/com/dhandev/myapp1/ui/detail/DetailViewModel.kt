package com.dhandev.myapp1.ui.detail

import android.content.Context
import androidx.lifecycle.*
import com.dhandev.myapp1.data.source.local.entity.CommentEntity
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.data.source.local.room.AppDatabase
import com.dhandev.myapp1.data.source.local.room.CommentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {
    private val _movieTvId = MutableLiveData<MovieEntity>()
    val movieTvId : LiveData<MovieEntity>
        get() = _movieTvId

    private val _commentById = MutableLiveData<List<CommentEntity>>()
    val commentById : LiveData<List<CommentEntity>>
        get() = _commentById

    fun insertFav(context: Context, data: MovieEntity){
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(context).movieDao().insertFav(data)
        }
    }

    fun getById(context: Context, owner: LifecycleOwner, id: Int){
        AppDatabase.getDatabase(context).movieDao().getById(id).observe(owner){result->
            _movieTvId.postValue(result)
        }
    }

    fun delete(context: Context, data: MovieEntity){
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(context).movieDao().deleteAll(data)
        }
    }

    //COMMENT
    fun getCommentById(context: Context, owner: LifecycleOwner, id: Int) {
        CommentDatabase.getDatabase(context).commentDao().getById(id).observe(owner) { result ->
            _commentById.postValue(result)
        }
    }
}