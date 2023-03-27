package com.dhandev.myapp1.data.source.remote.network

import com.dhandev.myapp1.data.source.remote.response.MovieTvResponse
import com.dhandev.myapp1.data.source.remote.response.PeopleDetailResponse
import com.dhandev.myapp1.data.source.remote.response.PeopleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("{endpoint}")
    fun getMovies(
        @Path("endpoint") sort: String,
        @Query("api_key") Authorization : String,
        @Query("language") lang: String,
        @Query("page") page: Int,
        @Query("query") query: String,
    ): Call<MovieTvResponse>

    @GET("person/popular")
    fun getPeople(
        @Query("api_key") Authorization : String,
        @Query("language") lang: String,
        @Query("page") page: Int,
    ): Call<PeopleResponse>

    @GET("person/{id}")
    fun getPeopleDetail(
        @Path("id") id: Int,
        @Query("api_key") Authorization : String,
        @Query("language") lang: String,
        @Query("page") page: Int,
    ): Call<PeopleDetailResponse>
}