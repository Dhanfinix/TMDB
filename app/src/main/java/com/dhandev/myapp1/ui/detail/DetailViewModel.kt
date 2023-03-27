package com.dhandev.myapp1.ui.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.data.source.local.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {

    fun insertFav(context: Context, data: MovieEntity){
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(context).movieDao().insertFav(data)
        }
    }

    fun getById(context: Context, id: Int): LiveData<MovieEntity> {
        val result = MutableLiveData<MovieEntity>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(AppDatabase.getDatabase(context).movieDao().getById(id))
        }
        return result
    }

    fun delete(context: Context, data: MovieEntity){
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(context).movieDao().deleteAll(data)
        }
    }
}