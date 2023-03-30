package com.dhandev.myapp1.data.source.remote.response

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class MovieTvResponse(

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem>? = null,

	@field:SerializedName("total_results")
	val totalResults: Int? = null
) : Parcelable

@Parcelize
@Serializable
data class ResultsItem(

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("original_title", alternate = ["original_name"])
	val originalTitle: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("release_date", alternate = ["first_air_date"])
	val releaseDate: String? = null,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@field:SerializedName("vote_average")
	val voteAverage: Double? = null,

	@field:SerializedName("popularity")
	val popularity: Double? = null,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int? = null,

) : Parcelable

