package com.dhandev.myapp1.di

import android.content.Context
import com.dhandev.myapp1.data.repository.MovieTvRepository
import com.dhandev.myapp1.data.source.local.room.AppDatabase
import com.dhandev.myapp1.data.source.remote.network.ApiConfig
import com.dhandev.myapp1.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context, endpoint: String, query: String): MovieTvRepository {
        val apiService = ApiConfig.getApiService()
        val database = AppDatabase.getDatabase(context)
        val dao = database.movieDao()
        val appExecutors = AppExecutors()
        println("ON Injection -> $endpoint & $query")
        return MovieTvRepository.getInstance(apiService, dao, appExecutors, endpoint, query)
    }
}