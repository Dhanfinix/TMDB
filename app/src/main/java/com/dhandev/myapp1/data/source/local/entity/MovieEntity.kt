package com.dhandev.myapp1.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movies")
data class MovieEntity(

    @ColumnInfo("overview")
    val overview: String? = null,

    @ColumnInfo("original_title")
    val originalTitle: String? = null,

    @ColumnInfo("title")
    val title: String? = null,

    @ColumnInfo("release_date")
    val releaseDate: String? = null,

    @ColumnInfo("poster_path")
    val posterPath: String? = null,

    @ColumnInfo("backdrop_path")
    val backdropPath: String? = null,

    @ColumnInfo("vote_average")
    val voteAverage: Double? = null,

    @ColumnInfo("popularity")
    val popularity: Double? = null,

    @PrimaryKey
    @ColumnInfo("id")
    val id: Int? = null,

    @ColumnInfo("type")
    val type: String? = null,

    @ColumnInfo("watchlist")
    val watchlist: Boolean = false,

) : Parcelable

@Entity
data class WatchlistUpdate(
    @ColumnInfo("id")
    val id: Int? = null,
    @ColumnInfo("watchlist")
    val watchlist: Boolean = false,
)