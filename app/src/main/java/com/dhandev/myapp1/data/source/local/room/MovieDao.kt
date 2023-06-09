package com.dhandev.myapp1.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dhandev.myapp1.data.source.local.entity.MovieEntity

@Dao
interface MovieDao {
    @Insert
    fun insertFav(data: MovieEntity)

    @Query("SELECT * FROM movies")
    fun getAllFav(): LiveData<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id=:ids")
    fun getById(ids:Int): LiveData<MovieEntity>

    @Delete
    fun deleteAll(data: MovieEntity)
}