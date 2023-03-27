package com.dhandev.myapp1.ui.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.data.source.local.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel: ViewModel() {
    private val _data = MutableLiveData<List<MovieEntity>>()
    val data : LiveData<List<MovieEntity>>
        get() = _data

    fun getFav(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            _data.postValue(AppDatabase.getDatabase(context).movieDao().getAllFav())
        }
    }
}