package com.example.tv_video_list.server

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ) : Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String
    ) : Response<MovieResponse>

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("api_key") apiKey: String
    ) : Response<GenreResponse>

    @GET("configuration")
    suspend fun getConfiguration(
        @Query("api_key") apiKey: String
    ) : Response<ConfigurationResponse>
}

data class ConfigurationResponse(val images: DetailsConfig)
data class DetailsConfig(
    val secure_base_url: String,
    val backdrop_sizes: List<String>
)


data class MovieResponse(val results: List<Movie>)
data class Movie(
    val id: Int,
    val title: String,
    val backdrop_path: String,
    val genre_ids: List<Int>
)

data class GenreResponse(val genres: List<Genre>)
data class Genre(
    val id: Int,
    val name: String
)
