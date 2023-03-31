package com.dhandev.myapp1.ui.list

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhandev.myapp1.BuildConfig
import com.dhandev.myapp1.data.source.remote.network.ApiConfig
import com.dhandev.myapp1.data.source.remote.response.MovieTvResponse
import com.dhandev.myapp1.data.source.remote.response.ResultsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel(context: Context, endpoint: String, query: String): ViewModel() {

    private val _movieTvData = MutableLiveData<List<ResultsItem>>()
    val movieTvData : LiveData<List<ResultsItem>>
        get() = _movieTvData

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg : LiveData<String>
        get() = _errorMsg

    init {
        getData(endpoint, query)
    }

    fun getData(path: String, query: String){
        ApiConfig.getApiService()
            .getMovies(path, BuildConfig.API_KEY, "en-US", 1, query)
            .enqueue(object : Callback<MovieTvResponse> {
                override fun onResponse(
                    call: Call<MovieTvResponse>,
                    response: Response<MovieTvResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val movieData = response.body()!!.results!!
                        _movieTvData.postValue(movieData)
                    } else {
                        _errorMsg.postValue(response.message())
                        Log.e("TAG", "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<MovieTvResponse>, t: Throwable) {
                    _errorMsg.postValue(t.message)
                    Log.d("Failure", t.message!!)
                }
            })
    }

}