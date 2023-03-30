package com.dhandev.myapp1.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dhandev.myapp1.data.source.local.entity.MovieEntity
import com.dhandev.myapp1.data.source.local.entity.WatchlistUpdate

@Dao
interface MovieDao {
    @Update(entity = MovieEntity::class)
    fun update(data: WatchlistUpdate)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies ORDER BY :sort DESC")
    fun getMovieTv(sort: String): LiveData<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE watchlist = 1")
    fun getAllFav(): LiveData<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id=:ids")
    fun getById(ids:Int): LiveData<MovieEntity>

    @Query("SELECT EXISTS(SELECT * FROM movies WHERE id = :ids AND watchlist = 1)")
    fun isMovieTvWatchlisted(ids: Int): Boolean

    @Delete
    fun deleteAll(data: MovieEntity)

    @Query("DELETE FROM movies WHERE watchlist = 0")
    fun deleteNotWatchlist()
}