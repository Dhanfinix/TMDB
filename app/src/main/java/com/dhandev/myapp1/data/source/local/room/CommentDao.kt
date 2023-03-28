package com.dhandev.myapp1.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dhandev.myapp1.data.source.local.entity.CommentEntity

@Dao
interface CommentDao {
    @Insert
    fun insertComment(data: CommentEntity)

    @Query("SELECT * FROM comment")
    fun getAllComment(): List<CommentEntity>

    @Query("SELECT * FROM comment WHERE movie_id = :movie_id")
    fun getById(movie_id:Int): LiveData<List<CommentEntity>>

    @Query("SELECT * FROM comment WHERE id = :id")
    fun getCommentId(id:Int): LiveData<CommentEntity>

    @Delete
    fun deleteComment(data: CommentEntity)

    @Update
    fun updateComment(data: CommentEntity)
}