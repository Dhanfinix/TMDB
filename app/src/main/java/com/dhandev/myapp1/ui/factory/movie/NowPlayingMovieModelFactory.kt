package com.dhandev.myapp1.ui.factory.movie

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dhandev.myapp1.ui.list.ListViewModel

class NowPlayingMovieModelFactory private constructor(private val context: Context, private val endpoint: String, private val query: String) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(context, endpoint, query) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: NowPlayingMovieModelFactory? = null
        fun getInstance(context: Context): NowPlayingMovieModelFactory =
            instance ?: synchronized(this) {
                instance ?: NowPlayingMovieModelFactory(context, "movie/now_playing", "")
            }.also { instance = it }
    }
}