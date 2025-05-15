package com.example.tv_video_list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tv_video_list.server.Movie
import com.example.tv_video_list.server.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val _moviesPopular = MutableStateFlow<List<Movie>>(emptyList())
    val moviesPopular: StateFlow<List<Movie>> = _moviesPopular.asStateFlow()
    private val _moviesTop = MutableStateFlow<List<Movie>>(emptyList())
    val moviesTop: StateFlow<List<Movie>> = _moviesTop.asStateFlow()

    val errorLivData = MutableLiveData("")

    fun fetchMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val popularMovies = RetrofitInstance.api.getPopularMovies(apiKey)
                    .body()?.results ?: emptyList()
                val topRatedMovies = RetrofitInstance.api.getTopRatedMovies(apiKey)
                    .body()?.results ?: emptyList()
                _moviesPopular.value = popularMovies
                _moviesTop.value = topRatedMovies
            } catch (e: Exception) {
                errorLivData.value = "Error fetching movies $e"
            }
        }
    }
}