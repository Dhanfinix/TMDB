package com.dhandev.myapp1.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dhandev.myapp1.data.source.local.entity.CommentEntity

@Dao
interface CommentDao {
    @Insert
    fun insertComment(data: CommentEntity)

    @Query("SELECT * FROM comment")
    fun getAllComment(): List<CommentEntity>

    @Query("SELECT * FROM comment WHERE movie_id = :movie_id")
    fun getById(movie_id:Int): LiveData<List<CommentEntity>>

    @Delete
    fun deleteAll(data: CommentEntity)
}