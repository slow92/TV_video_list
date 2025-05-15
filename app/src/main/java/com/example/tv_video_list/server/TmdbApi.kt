package com.example.tv_video_list.server

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String
    ) : Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") apiKey: String
    ) : Response<MovieResponse>
}

data class MovieResponse(val results: List<Movie>)
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String
)
