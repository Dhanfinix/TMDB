package com.dhandev.myapp1.ui.watchlist

import android.content.Context
import androidx.lifecycle.*
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.data.source.local.entity.WatchlistUpdate
import com.dhandev.myapp1.data.source.local.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatchlistViewModel: ViewModel() {
    private val _data = MutableLiveData<List<MovieEntity>>()
    val data : LiveData<List<MovieEntity>>
        get() = _data

    fun updateWatchlist(context: Context, data: WatchlistUpdate){
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(context).movieDao().update(data)
        }
    }

    fun getFav(context: Context, owner: LifecycleOwner){
        AppDatabase.getDatabase(context).movieDao().getAllFav().observe(owner){
            _data.postValue(it)
        }
    }

    fun delete(context: Context, data: MovieEntity){
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(context).movieDao().deleteAll(data)
        }
    }

    fun clearDatabase(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(context).clearAllTables()
        }
    }
}