package com.dhandev.myapp1.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dhandev.myapp1.BuildConfig
import com.dhandev.myapp1.data.Result
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.data.source.local.room.MovieDao
import com.dhandev.myapp1.data.source.remote.network.ApiService
import com.dhandev.myapp1.data.source.remote.response.MovieTvResponse
import com.dhandev.myapp1.utils.AppExecutors
import com.dhandev.myapp1.utils.TypeEnum
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieTvRepository private constructor(
    private val apiService: ApiService,
    private val movieDao: MovieDao,
    private val appExecutors: AppExecutors,
    private val endpoint: String,
    private val query: String
){
    private val result = MediatorLiveData<Result<List<MovieEntity>>>()

    fun getMovieTvData(): LiveData<Result<List<MovieEntity>>>{
        val type = if (endpoint.contains("movie")) TypeEnum.MOVIE.body else TypeEnum.TV.body

        result.value = Result.Loading
        println("ON Repo -> $endpoint & $query")
        val client = apiService.getMovies(endpoint, BuildConfig.API_KEY, "en-US", 1, query)
        client.enqueue(object : Callback<MovieTvResponse>{
            override fun onResponse(
                call: Call<MovieTvResponse>,
                response: Response<MovieTvResponse>
            ) {
                if (response.isSuccessful){
                    val movieTv = response.body()?.results
                    val movieTvList = ArrayList<MovieEntity>()
                    appExecutors.diskIO.execute{
                        movieTv?.forEach {resultsItem ->
                            val isWatchListed = movieDao.isMovieTvWatchlisted(resultsItem.id!!)

                            val data = MovieEntity(
                                resultsItem.overview,
                                resultsItem.originalTitle,
                                resultsItem.title,
                                resultsItem.releaseDate,
                                resultsItem.posterPath,
                                resultsItem.backdropPath,
                                resultsItem.voteAverage,
                                resultsItem.id,
                                type,
                                isWatchListed
                            )
                            movieTvList.add(data)
                        }
                        movieDao.deleteNotWatchlist()
                        movieDao.insertMovie(movieTvList)
                    }
                }
            }

            override fun onFailure(call: Call<MovieTvResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })
        val localData = movieDao.getMovieTv()
        result.addSource(localData){newData : List<MovieEntity> ->
            result.value = Result.Success(newData)
        }
        return result
    }


    companion object {
        @Volatile
        private var instance: MovieTvRepository? = null
        fun getInstance(
            apiService: ApiService,
            movieDao: MovieDao,
            appExecutors: AppExecutors,
            endpoint: String,
            query: String
        ): MovieTvRepository =
            instance ?: synchronized(this) {
                instance ?: MovieTvRepository(apiService, movieDao, appExecutors, endpoint, query)
            }.also { instance = it }
    }
}