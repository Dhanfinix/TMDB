package com.dhandev.myapp1.ui.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.dhandev.myapp1.BuildConfig
import com.dhandev.myapp1.data.source.local.entity.CommentEntity
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.data.source.local.entity.WatchlistUpdate
import com.dhandev.myapp1.data.source.local.room.AppDatabase
import com.dhandev.myapp1.data.source.local.room.CommentDatabase
import com.dhandev.myapp1.data.source.remote.network.ApiConfig
import com.dhandev.myapp1.data.source.remote.response.ResultsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {
    private val _watchlisted = MutableLiveData<Boolean>()
    val watchlisted : LiveData<Boolean>
        get() = _watchlisted

    private val _commentById = MutableLiveData<List<CommentEntity>>()
    val commentById : LiveData<List<CommentEntity>>
        get() = _commentById


    private val _movieTvData = MutableLiveData<ResultsItem>()
    val movieTvData : LiveData<ResultsItem>
        get() = _movieTvData

    fun updateWatchlist(context: Context, data: WatchlistUpdate){
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(context).movieDao().update(data)
        }
    }

    fun getWatchlist(context: Context, id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _watchlisted.postValue(AppDatabase.getDatabase(context).movieDao().isMovieTvWatchlisted(id))
        }

    }

    fun delete(context: Context, data: MovieEntity){
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(context).movieDao().deleteAll(data)
        }
    }

    fun getData(path: String, onFailedCallback: (String) -> Unit){
        ApiConfig.getApiService()
            .getMoviesOne(path, BuildConfig.API_KEY, "en-US", 1)
            .enqueue(object : Callback<ResultsItem> {
                override fun onResponse(
                    call: Call<ResultsItem>,
                    response: Response<ResultsItem>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        _movieTvData.postValue(response.body())
                    } else {
                        onFailedCallback(response.message())
                        Log.e("TAG", "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<ResultsItem>, t: Throwable) {
                    onFailedCallback(t.message!!)
                    Log.d("Failure", t.message!!)
                }
            })
    }

    //COMMENT
    fun getCommentById(context: Context, owner: LifecycleOwner, id: Int) {
        CommentDatabase.getDatabase(context).commentDao().getById(id).observe(owner) { result ->
            _commentById.postValue(result)
        }
    }
}