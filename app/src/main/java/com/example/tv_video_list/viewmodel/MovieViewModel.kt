package com.example.tv_video_list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tv_video_list.R
import com.example.tv_video_list.server.DetailsConfig
import com.example.tv_video_list.server.Genre
import com.example.tv_video_list.server.Movie
import com.example.tv_video_list.server.RetrofitInstance
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class MovieViewModel : ViewModel() {
    private val exceptionHandler: CoroutineContext =
        CoroutineExceptionHandler { _ , throwable ->
            errorLivData.value = R.string.tmdb_connection_error
            throwable.printStackTrace()
        }
     private val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO + exceptionHandler

    private val _configDetails = MutableStateFlow<DetailsConfig?>(null)
    val configDetails: StateFlow<DetailsConfig?> = _configDetails.asStateFlow()
    private val _moviesPopular = MutableStateFlow<List<Movie>>(emptyList())
    val moviesPopular: StateFlow<List<Movie>> = _moviesPopular.asStateFlow()
    private val _moviesTop = MutableStateFlow<List<Movie>>(emptyList())
    val moviesTop: StateFlow<List<Movie>> = _moviesTop.asStateFlow()
    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

    val errorLivData = MutableLiveData<Int>()

    init {
        getMovieGenres()
        getConfigDetails()
        fetchTopRateMovies()
        fetchPopularMovies()
    }

    fun getMovieGenres() {
        viewModelScope.launch(coroutineContext) {
            val genresListResponse = RetrofitInstance.api.getMovieGenres(API_KEY)
            if (genresListResponse.isSuccessful) {
                _genres.value= genresListResponse.body()?.genres ?: emptyList()
            }
        }
    }

    fun getConfigDetails() {
        viewModelScope.launch(coroutineContext) {
            val configResponse = RetrofitInstance.api.getConfiguration(API_KEY)
            if (configResponse.isSuccessful) {
                _configDetails.value =
                    configResponse.body()?.images ?: DetailsConfig("", emptyList())
            }
        }
    }

    fun fetchTopRateMovies() {
        viewModelScope.launch(coroutineContext) {
            val topRatedMoviesResponse = RetrofitInstance.api.getTopRatedMovies(API_KEY)
            if (topRatedMoviesResponse.isSuccessful) {
                _moviesTop.value =
                    topRatedMoviesResponse.body()?.results ?: emptyList()
            }
        }
    }

    fun fetchPopularMovies() {
        viewModelScope.launch(coroutineContext) {
            val popularMoviesResponse = RetrofitInstance.api.getPopularMovies(API_KEY)
            if (popularMoviesResponse.isSuccessful) {
                _moviesPopular.value =
                    popularMoviesResponse.body()?.results ?: emptyList()
            }
        }
    }

    companion object {
        const val API_KEY: String = "."
    }
}