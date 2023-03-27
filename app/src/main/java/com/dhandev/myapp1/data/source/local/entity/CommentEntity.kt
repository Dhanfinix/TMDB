package com.dhandev.myapp1.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "comment")
data class CommentEntity(

    @ColumnInfo("username")
    val username: String? = null,

    @ColumnInfo("comment_body")
    val commentBody: String? = null,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int = 0,

    @ColumnInfo("movie_id")
    val movieId: Int? = null,
) : Parcelable
