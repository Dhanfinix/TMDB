package com.dhandev.myapp1.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dhandev.myapp1.data.repository.MovieTvRepository
import com.dhandev.myapp1.di.Injection
import com.dhandev.myapp1.ui.list.ListViewModel

class ListViewModelFactory private constructor(private val newsRepository: MovieTvRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ListViewModelFactory? = null
        fun getInstance(context: Context, endpoint: String, query: String): ListViewModelFactory =
            instance ?: synchronized(this) {
                println("ON Factory -> $endpoint & $query")
                instance ?: ListViewModelFactory(Injection.provideRepository(context, endpoint, query))
            }.also { instance = it }
    }
}