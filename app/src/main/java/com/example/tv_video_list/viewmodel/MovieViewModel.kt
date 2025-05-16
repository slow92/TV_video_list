package com.example.tv_video_list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tv_video_list.R
import com.example.tv_video_list.server.DetailsConfig
import com.example.tv_video_list.server.Genre
import com.example.tv_video_list.server.Movie
import com.example.tv_video_list.server.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MovieViewModel : ViewModel() {
    private val _configDetails = MutableStateFlow<DetailsConfig?>(null)
    val configDetails: StateFlow<DetailsConfig?> = _configDetails.asStateFlow()
    private val _moviesPopular = MutableStateFlow<List<Movie>>(emptyList())
    val moviesPopular: StateFlow<List<Movie>> = _moviesPopular.asStateFlow()
    private val _moviesTop = MutableStateFlow<List<Movie>>(emptyList())
    val moviesTop: StateFlow<List<Movie>> = _moviesTop.asStateFlow()
    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

    val errorLivData = MutableLiveData<Int>()

    fun fetchMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val popularMovies = RetrofitInstance.api.getPopularMovies(apiKey)
                    .body()?.results ?: emptyList()
                val topRatedMovies = RetrofitInstance.api.getTopRatedMovies(apiKey)
                    .body()?.results ?: emptyList()
                val genresList = RetrofitInstance.api.getMovieGenres(apiKey)
                    .body()?.genres ?: emptyList()
                val config = RetrofitInstance.api.getConfiguration(apiKey)
                    .body()?.images ?: DetailsConfig("", emptyList())
                _configDetails.value = config
                _moviesPopular.value = popularMovies
                _moviesTop.value = topRatedMovies
                _genres.value = genresList
            } catch (_: Exception) {
                errorLivData.value = R.string.fetching_movies_error
            }
        }
    }
}