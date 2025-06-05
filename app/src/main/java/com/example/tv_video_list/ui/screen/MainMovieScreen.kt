package com.example.tv_video_list.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
//import androidx.tv.material3.MaterialTheme
//import androidx.tv.material3.Text
import com.example.tv_video_list.MainActivity.MoviesCategory
import com.example.tv_video_list.R
import com.example.tv_video_list.ui.component.MovieCard
import com.example.tv_video_list.viewmodel.MovieViewModel

@Composable
fun MovieScreen(viewModel: MovieViewModel) {
    val ctx = LocalContext.current
    val moviesPopular by viewModel.moviesPopular.collectAsState()
    val moviesTop by viewModel.moviesTop.collectAsState()
    val genresList by viewModel.genres.collectAsState()
    val configDetails by viewModel.configDetails.collectAsState()
    var imageUrlPrefix: String? = configDetails?.secure_base_url +
            configDetails?.backdrop_sizes?.last()

    val moviesData = listOf(
        MoviesCategory(getString(ctx, R.string.most_popular), 240, moviesPopular),
        MoviesCategory(getString(ctx, R.string.top_rated), 200, moviesTop),
        MoviesCategory(getString(ctx, R.string.most_popular), 500, moviesPopular)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(moviesData) { category ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = category.title,
                color = Color.Gray,
                fontSize = 20.sp,
                style = MaterialTheme.typography.h3
            )

            imageUrlPrefix?.let {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(category.moviesList) { movie ->
                        val genreString = genresList
                            .filter { it.id in movie.genre_ids }.joinToString(", ") { it.name }
                        MovieCard(
                            movie = movie,
                            size = category.size,
                            genreString = genreString,
                            imageUrlPrefix = imageUrlPrefix
                        )
                    }
                }
            }
        }
    }
}