package com.dhandev.myapp1.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tv_show")
data class TvEntity(

    @ColumnInfo("overview")
    val overview: String? = null,

    @ColumnInfo("original_name")
    val originalTitle: String? = null,

    @ColumnInfo("title")
    val title: String? = null,

    @ColumnInfo("poster_path")
    val posterPath: String? = null,

    @ColumnInfo("backdrop_path")
    val backdropPath: String? = null,

    @ColumnInfo("vote_average")
    val voteAverage: Double? = null,

    @PrimaryKey
    @ColumnInfo("id")
    val id: Int? = null,

) : Parcelable
